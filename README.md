# java-ddd-demo

Spring Boot 3.2 + DDD 六模块架构示例，包含两套仓储风格的对比实现。

## 技术栈

- Java 21 / Spring Boot 3.2.3
- Spring Data JPA (Hibernate 6.4)
- PostgreSQL
- JUnit 5 + AssertJ

## 模块结构

```
demo/
├── demo-common/         公共层 — 异常、事件基类、日志注解
├── demo-domain/         领域层 — 聚合根、值对象、仓储接口、领域服务
├── demo-application/    应用层 — AppService、DTO、Converter
├── demo-infrastructure/ 基础设施层 — JPA 实现、MQ、外部服务
├── demo-interface/      接口层 — REST Controller、全局异常处理
└── demo-start/          启动层 — 启动类、配置、数据库建表
```

## 快速开始

```bash
# 1. 确保 PostgreSQL 运行，创建数据库
createdb ddd_demo

# 2. 修改数据库连接（如需要）
# demo-start/src/main/resources/application.yml

# 3. 启动
./mvnw spring-boot:run -pl demo-start

# 4. 测试
./mvnw test
```

## API 概览

### Skill (风格一：PO/Domain 分离)

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/skills` | 创建技能 |
| GET | `/api/skills/{id}` | 查询单个 |
| GET | `/api/skills` | 分页查询 |
| PUT | `/api/skills/{id}` | 更新详情 |
| PUT | `/api/skills/{id}/name` | 修改名称 |
| PUT | `/api/skills/{id}/status` | 状态变更 |
| POST | `/api/skills/{id}/activate` | 启用 |
| POST | `/api/skills/{id}/archive` | 归档 |
| POST | `/api/skills/batch-archive` | 批量归档 |
| DELETE | `/api/skills/{id}` | 删除 |

### Project

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/projects` | 创建项目 |
| GET | `/api/projects/{id}` | 查询单个 |
| GET | `/api/projects` | 查询全部 |
| PUT | `/api/projects/{id}` | 更新 |
| POST | `/api/projects/{id}/activate` | 激活 |
| POST | `/api/projects/{id}/complete` | 完成 |
| DELETE | `/api/projects/{id}` | 删除 |

### Order

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/orders` | 创建订单 |
| GET | `/api/orders/{id}` | 查询单个 |
| POST | `/api/orders/{id}/pay` | 支付 |
| DELETE | `/api/orders/{id}` | 删除 |

## 仓库风格对比

项目内并存两套仓储实现，详见[文档](docs/ddd-repository-styles.md)。

| | 风格一 (Skill) | 风格二 (Skill2) |
|---|---|---|
| 领域模型 | 纯 POJO | JPA Entity 贴 @Entity |
| PO 类 | 有 | 无 |
| Converter | 有（toDomain / toPO） | 无 |
| Repository | 手写 Impl 类 | JpaRepository 自动生成 |
| 精准更新 | native SQL | JPQL @Modifying |
| 额外文件 | 6 个 | 0 个 |

## DDD 核心实践

- **充血模型** — 行为方法在聚合根内（`activate()` / `pay()` / `archive()`），不散落 Service
- **工厂方法** — `Skill.register()` / `Order.place()` / `Project.initiate()` 替代 new + setter
- **reconstitute()** — 从持久层重建聚合根，Infra Converter 不调业务构造器
- **领域事件** — 状态变更触发事件，事务提交后发布
- **CQRS** — 读仓库返回轻量投影，写仓库操作聚合根
- **状态机** — 状态变更枚举 + switch 校验合法路径
