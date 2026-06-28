package com.example.infrastructure.skill2;

import com.example.domain.skill2.model.Skill2Entity;
import com.example.domain.skill2.model.Skill2Entity.Status;
import com.example.domain.skill2.repository.Skill2Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 风格二：一个接口同时继承 JpaRepository + 领域仓储接口。
 *
 * <p><b>为什么不需要 Impl 类？</b>
 * Spring Data JPA 在启动时为每个 JpaRepository 子接口动态生成代理实现：
 * <ol>
 *   <li>{@code save/delete} —— 自动生成</li>
 *   <li>{@code findById/findAll} —— 自动生成</li>
 *   <li>{@code findByCategory} —— 方法名即 SQL，Spring Data 解析生成</li>
 *   <li>{@code updateName/updateStatus} —— 手写 JPQL + @Modifying，不走全量 save</li>
 * </ol>
 *
 * <p><b>和风格一的关键差异：</b>
 * <ul>
 *   <li>风格一 {@code SkillRepositoryImpl}：手写类实现，依赖 EntityManager + JpaRepository，</li>
 *      每个方法自己写 JPQL 或 native SQL。
 *   <li>风格二这个接口：零行实现代码，Spring Data 全自动生成。</li>
 * </ul>
 *
 * <p><b>精准字段更新的写法和原理：</b>
 * <ul>
 *   <li>用 {@code @Modifying @Query("UPDATE Entity SET field = :val WHERE id = :id")}，</li>
 *      生成 {@code UPDATE t_skill2 SET name = ? WHERE id = ?}，只改目标列。
 *   <li>不走 {@code findById → 改字段 → save}，避免读-改-写的并发覆盖风险。</li>
 *   <li>背后的 SQL 和手写 native query 完全一样，但 JPQL 写的是类名/字段名，</li>
 *      编译期就能检查语法，IDE 支持重构。
 * </ul>
 */
@Repository
public interface Skill2JpaRepository extends JpaRepository<Skill2Entity, Long>, Skill2Repository {

    // ── 以下方法都是 JpaRepository 自动生成的，不需要写代码 ──
    // save(Skill2Entity)  /  findById(Long)  /  findAll()  /  delete(Skill2Entity)

    /**
     * 方法名即查询 —— Spring Data 自动解析 {@code findBy + Category}。
     * 等价于 JPQL: {@code SELECT s FROM Skill2Entity s WHERE s.category = :category}
     */
    @Override
    List<Skill2Entity> findByCategory(String category);

    /**
     * 精准更新名字 —— 只改 name 列。
     * <pre>
     * 生成的 SQL:
     * UPDATE t_skill2 SET name = ? WHERE id = ?
     * </pre>
     * 不走 findById → setName → save 的全量 UPDATE。
     */
    @Override
    @Modifying // ← 告诉 Spring Data 这是写操作，不是查询
    @Query("UPDATE Skill2Entity s SET s.name = :name WHERE s.id = :id")
    int updateName(@Param("id") Long id, @Param("name") String name);

    /**
     * 精准更新状态 —— 只改 status 列。
     * <pre>
     * 生成的 SQL:
     * UPDATE t_skill2 SET status = ? WHERE id = ?
     * </pre>
     */
    @Override
    @Modifying
    @Query("UPDATE Skill2Entity s SET s.status = :status WHERE s.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") Status status);
}
