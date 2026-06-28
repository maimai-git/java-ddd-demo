# DDD 仓储设计：两种风格对比

项目内并存两套 DDD 仓储实现，分别在 `demo-domain/skill*` 和 `demo-domain/skill2*` 下，方便对比和维护时参考。

## 架构全景图

```
                    风格一（Skill）：PO ↔ Domain 分离

    领域层                           基础设施层
    ──────                           ──────
┌──────────────┐              ┌──────────────┐     ┌──────────────┐
│ Skill.java    │              │ SkillPO.java  │     │ SkillJpaRepo │
│ (纯 POJO)     │              │ (@Entity)     │────→│ (JpaRepository)
│              │              │              │     └──────────────┘
│ name: String │              │ name: String │
│ status: Enum │              │ status: String│    ┌──────────────┐
│ createdAt    │              │ created_at    │    │ EntityManager │
│              │              │              │────→│ (原生 SQL)    │
│ 行为方法:     │              │ (无行为方法)  │     └──────────────┘
│ activate()   │              └──────────────┘
│ changeName() │                    ↑  ↑
└──────────────┘                    │  │
       ↑                            │  │
       │          ┌─────────────────┘  │
       │          │  ┌─────────────────┘
       │    toDomain()  toPO()
┌──────────────┐   ┌──────────────────────────────────────┐
│ AppService   │   │ SkillConverter         SkillReadConverter │
│              │   │                                       │
│ ① findById  │   │  toPO(domain) → PO    toReadModel(po) │
│ ② 调行为方法 │   │  toDomain(po) → domain                 │
│ ③ repo.save │   │                                       │
└──────────────┘   └──────────────────────────────────────┘
                          ↑ 每次读写都要转 ── 6 个额外文件


                    风格二（Skill2）：JPA Entity 即领域模型

    领域层                           基础设施层
    ──────                           ──────
┌──────────────┐              ┌──────────────────────────────────────┐
│Skill2Entity  │              │Skill2JpaRepository                    │
│(@Entity)     │──────────────│(extends JpaRepository + Skill2Repo)  │
│              │   直接操作     │                                      │
│ name: String │              │ 接口声明，零实现代码:                   │
│ status: Enum │              │                                      │
│ createdAt    │              │ @Modifying                           │
│              │              │ @Query("UPDATE ... SET name=? WHERE")│
│ 行为方法:     │              │ int updateName(id, name);  ← JPQL   │
│ activate()   │              └──────────────────────────────────────┘
│ changeName() │
└──────────────┘              ┌──────────────────────────────────────┐
       ↑                      │Skill2QueryJpaRepository               │
       │                      │(读仓库 CQRS)                          │
┌──────────────┐              │                                      │
│ AppService   │              │ JPQL 构造器  一句映射:                 │
│              │              │ SELECT new ProjectionImpl(id,name...) │
│ ① findById  │              │ FROM Skill2Entity                    │
│ ② 调行为方法 │              └──────────────────────────────────────┘
│ ③ updateName │
└──────────────┘               ── 无 PO 类、无 Converter、无 Impl 类
                                 额外文件: 0
```

## 请求调用链

### 风格一：PUT /api/skills/{id}/name

```
 Controller          Application              Domain                  Infrastructure
   层                  层                      层                        层
   │                  │                       │                        │
┌──────────┐      ┌──────────┐          ┌──────────┐            ┌──────────────┐
│ PUT      │      │ SkillApp │          │ Skill    │            │ SkillRepo    │
│ /api/    │─────→│ Service  │          │ Domain   │            │ Impl         │
│ skills/  │      │          │          │ Service  │            │              │
│ {id}/    │      │  findById│          │          │            │ jpaRepo      │
│ name     │      │  (id)    │─────────→│ findById │───────────→│ .findById(id)│
│          │      │          │          │  (id)    │            │  ↓           │
│          │      │          │          │          │            │ SkillPO ───┐ │
│          │      │          │          │          │←───────────│ SkillConverter  │
│          │      │          │          │          │            │ .toDomain() │ │
│          │      │          │          │  Skill   │            │  ↓           │
│          │      │          │←─────────│ (领域对象) │            │ SkillPO      │
│          │      │          │          │          │            └──────────────┘
│          │      │  skill.  │          │          │
│          │      │  change  │          │ change   │
│          │      │  Name()  │─────────→│ Name()   │
│          │      │          │          │          │
│          │      │          │          │          │
│          │      │  domain  │          │          │
│          │      │  Service │          │          │
│          │      │  .save() │─────────→│ save()   │───────────→ ① toPO(skill) → SkillPO
│          │      │          │          │          │            │ ② jpa.save(po)
│          │      │          │          │          │            │ ③ toDomain(saved)
│          │      │          │          │          │←───────────│ ④ 返回 Skill
│          │      │←─────────│ Response │          │            └──────────────┘
│          │←─────│          │          │          │
└──────────┘      └──────────┘          └──────────┘

  每一步读/写都要 PO ↔ Domain 双向转换
  额外文件: SkillPO, SkillConverter, SkillRepositoryImpl
```

### 风格二：PUT /api/skill2/{id}/name

```
 Controller          Application              Domain                  Infrastructure
   层                  层                      层                        层
   │                  │                       │                        │
┌──────────┐      ┌──────────┐          ┌──────────┐            ┌──────────────┐
│ PUT      │      │ Skill2   │          │ Skill2   │            │ Skill2Jpa    │
│ /api/    │      │ App      │          │ Domain   │            │ Repository   │
│ skill2/  │      │ Service  │          │ Service  │            │ (接口,零实现) │
│ {id}/    │      │          │          │  (接口)   │            │              │
│ name     │      │  domain  │          │          │            │ Spring Data  │
│          │      │  Service │          │          │            │ 自动提供:    │
│          │      │  .find   │          │          │            │ findById()   │
│          │      │  ById()  │─────────→│ findById │───────────→│ 返回 Entity  │
│          │      │          │          │          │←───────────│ 无需转换     │
│          │      │          │          │ Entity   │            └──────────────┘
│          │      │  entity  │          │          │
│          │      │  .change │          │ change   │
│          │      │  Name()  │─────────→│ Name()   │
│          │      │          │          │          │
│          │      │  domain  │          │          │            ┌──────────────┐
│          │      │  Service │          │          │            │ Skill2Jpa    │
│          │      │  .update │          │          │            │ Repository   │
│          │      │  Name()  │─────────→│ update   │───────────→│ @Modifying   │
│          │      │          │          │ Name()   │            │ @Query(UPDATE│
│          │      │          │          │          │            │  SET name=?  │
│          │      │          │          │          │            │  WHERE id=?) │
│          │      │←─────────│ Response │          │            │ 只改1列      │
│          │←─────│          │          │          │            └──────────────┘
└──────────┘      └──────────┘          └──────────┘

  零次转换，零个 Impl 类
  额外文件: 0
```

## 文件清单对比

| 层 | 风格一 (Skill) | 风格二 (Skill2) |
|----|---------------|----------------|
| domain model | `Skill.java`（纯 POJO，继承 BaseEntity） | `Skill2Entity.java`（贴 @Entity） |
| domain repo | `SkillRepository.java` | `Skill2Repository.java` |
| domain service | `SkillDomainService.java`（具体类） | `Skill2DomainService.java`（接口） + `impl/Skill2DomainServiceImpl.java` |
| domain read | `SkillReadModel.java`（record） | `Skill2Projection.java`（interface） |
| domain read repo | `SkillReadRepository.java` | `Skill2QueryRepository.java` |
| infra PO | `SkillPO.java` | 无 |
| infra converter | `SkillConverter.java` + `SkillReadConverter.java` | 无 |
| infra repo impl | `SkillRepositoryImpl.java`（手写 ~50行） + `SkillReadRepositoryImpl.java`（手写 ~70行） | `Skill2JpaRepository.java`（声明式，~40行） + `Skill2QueryJpaRepository.java`（~30行） |
| **额外文件数** | **6 个（PO×1 + Converter×2 + Impl×2 + ReadModel×1）** | **0 个** |

## 精准字段更新

```java
// 风格一：手写 native SQL
em.createNativeQuery("UPDATE t_skill SET name = :name, updated_at = :now WHERE id = :id")
  .setParameter("name", name)
  .setParameter("now", updatedAt)
  .setParameter("id", id)
  .executeUpdate();

// 风格二：声明式 JPQL
@Modifying
@Query("UPDATE Skill2Entity s SET s.name = :name WHERE s.id = :id")
int updateName(@Param("id") Long id, @Param("name") String name);
```

效果完全一样：生成 `UPDATE t_skill2 SET name = ? WHERE id = ?`，只改目标列，不会有并发覆盖风险。

## CQRS 读模型

```java
// 风格一：查 PO → 手动映射 record
SkillPO po = em.find(SkillPO.class, id);
return new SkillReadModel(po.getId(), po.getName(), po.getCategory(), ...);

// 风格二：JPQL 构造器，一句映射
SELECT new com.example...Skill2ProjectionImpl(s.id, s.name, s.category, s.status)
FROM Skill2Entity s WHERE ...
```

## 选型建议

| 场景 | 推荐 |
|------|------|
| 团队熟悉 Spring Data JPA，不想多写文件 | 风格二 |
| 追求 DDD 纯粹性，领域层零框架依赖 | 风格一 |
| 未来可能换持久层框架（MyBatis、NoSQL） | 风格一 |
| 快速迭代、代码量敏感 | 风格二 |
| 聚合根极度复杂（大量值对象嵌套） | 风格一（PO 可灵活设计） |
