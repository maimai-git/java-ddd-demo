package com.example.domain.skill2.repository;

/**
 * CQRS 读投影接口 —— 只定义列表需要的字段。
 *
 * <p>和风格一 {@code SkillReadModel} 的对比：
 * <ul>
 *   <li><b>SkillReadModel</b>：是一个 record，infra 层查到 SkillPO 后手动 new SkillReadModel(...)。</li>
 *   <li><b>Skill2Projection</b>：是一个 interface，infra 用 JPQL
 *       {@code SELECT new Skill2ProjectionImpl(...)} 直接映射，</li>
 *       不加载完整 Entity，跳过 description 等大字段。</li>
 * </ul>
 *
 * <p>接口定义在领域层，具体实现（Skill2ProjectionImpl）在 infra 层。
 */
public interface Skill2Projection {
    Long getId();
    String getName();
    String getCategory();
    String getStatus();
}
