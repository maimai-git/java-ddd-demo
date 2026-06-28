package com.example.domain.skill2.model;

import com.example.common.exception.BizException;
import com.example.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * 风格二：JPA 实体即领域模型。
 *
 * <p>核心差异 —— 和 {@link com.example.domain.skill.model.Skill} 对比：
 * <ul>
 *   <li><b>Skill（风格一）</b>：领域对象是纯 POJO，infra 层有 SkillPO + SkillConverter，</li>
 *      每个 save/findById 都要 toPO/toDomain 双向转换。
 *   <li><b>Skill2Entity（风格二）</b>：JPA 注解直接贴在领域对象上，</li>
 *      没有单独的 PO 类和 Converter，Spring Data JPA 直接操作领域对象。
 * </ul>
 *
 * <p>代价：领域层依赖了 jakarta.persistence 包，不再是纯内存模型。
 * 好处：少写一半文件，查改即刻返回实体，无需转换。
 */
@Entity
@Table(name = "t_skill2")
public class Skill2Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增，由数据库生成
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING) // 枚举存字符串，避免用 0/1 序号导致重排后数据不一致
    private Level level;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant createdAt;
    private Instant updatedAt;

    public enum Level { BEGINNER, INTERMEDIATE, ADVANCED, EXPERT }
    public enum Status { DRAFT, ACTIVE, ARCHIVED }

    // JPA 要求无参构造器，Hibernate 通过反射创建实例后 set 值
    protected Skill2Entity() {}

    /**
     * 工厂方法 —— 代替直接 new + 逐个 setter。
     * 内部完成字段校验 + 默认值设置 + 时间戳初始化。
     */
    public static Skill2Entity create(String name, String category, Level level, String description) {
        Skill2Entity e = new Skill2Entity();
        if (name == null || name.isBlank()) throw new BizException(ErrorCode.SKILL_NAME_REQUIRED);
        if (category == null || category.isBlank()) throw new BizException(ErrorCode.SKILL_CATEGORY_REQUIRED);
        e.name = name;
        e.category = category;
        e.level = level != null ? level : Level.BEGINNER;
        e.description = description;
        e.status = Status.DRAFT;
        var now = Instant.now();
        e.createdAt = now;
        e.updatedAt = now;
        return e;
    }

    /* ===== 充血行为方法 ===== */

    /**
     * 改名 —— 业务校验放在实体内部，不散落在 Service 里。
     * 外部只需调 {@code entity.changeName("新名")}，不需要知道有哪些字段要联动。
     */
    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) throw new BizException(ErrorCode.SKILL_NAME_REQUIRED);
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    /**
     * 状态机变更 —— 限制合法路径。
     * <pre>
     * DRAFT  → ACTIVE   （启用）
     * ACTIVE → ARCHIVED （归档）
     * ARCHIVED 禁止任何变更
     * </pre>
     */
    public void changeStatus(Status newStatus) {
        if (newStatus == null || newStatus == this.status) return;
        switch (this.status) {
            case DRAFT -> {
                if (newStatus != Status.ACTIVE) throw new BizException(ErrorCode.INVALID_PARAM, "草稿只能切为启用");
            }
            case ACTIVE -> {
                if (newStatus != Status.ARCHIVED) throw new BizException(ErrorCode.INVALID_PARAM, "启用只能切为归档");
            }
            case ARCHIVED -> throw new BizException(ErrorCode.INVALID_PARAM, "已归档不可变更");
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Level getLevel() { return level; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
