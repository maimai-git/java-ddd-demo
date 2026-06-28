# java-ddd-demo

Spring Boot 3.2 + DDD six-module architecture demo, with two repository implementation styles compared side by side.

Spring Boot 3.2 + DDD 六模块架构示例，包含两套仓储风格的对比实现。

## Architecture 架构

```
                      ┌──────────────────────────────────────────────────────┐
                      │                  demo-interface                       │
                      │   REST Controller / GlobalExceptionHandler / AOP     │
                      └──────────┬───────────────────────────────┬───────────┘
                                 │                               │
                          ┌──────▼──────┐                  ┌─────▼──────┐
                          │  demo-start  │                  │    DTO     │
                          │  Boot App    │                  │  Command   │
                          │  Config      │                  │  Query     │
                          └──────┬───────┘                  │  CO / Resp │
                                 │                          └─────┬──────┘
                                 │                                │
                      ┌──────────▼────────────────────────────────▼──────────┐
                      │                  demo-application                     │
                      │          AppService  (orchestration only)             │
                      │          Converter (DTO ↔ Domain)                    │
                      └──────────┬───────────────────────────────┬───────────┘
                                 │                               │
                    ┌────────────▼────────────┐      ┌───────────▼───────────┐
                    │     demo-domain          │      │   demo-infrastructure │
                    │                          │      │                       │
                    │  Aggregate Root          │      │  JPA Impl / PO        │
                    │  Value Object            │      │  MQ Producer           │
                    │  Domain Service          │◄─────│  External API Client   │
                    │  Repository Interface    │      │                       │
                    │  Domain Event            │      └───────────────────────┘
                    └────────────┬────────────┘
                                 │
                    ┌────────────▼────────────┐
                    │     demo-common          │
                    │  BizException / ErrorCode│
                    │  @BizLog / DomainEvent   │
                    └──────────────────────────┘
```

### Dependency 依赖方向

```
interface → application → domain ← infrastructure
                              ↑
                          common (shared by all)
```

## Quick Start 快速开始

```bash
# 1. Start PostgreSQL & create database / 启动 PostgreSQL 并建库
createdb ddd_demo

# 2. Edit connection if needed / 如需修改数据库连接
#    demo-start/src/main/resources/application.yml

# 3. Run / 启动
./mvnw spring-boot:run -pl demo-start

# 4. Test / 测试
./mvnw test
```

## Module Overview 模块概览

| Module 模块 | Layer 层 | Responsibility 职责 |
|------------|----------|---------------------|
| `demo-common` | Shared | Exception / Event base / Log annotation |
| `demo-domain` | Domain | Aggregate / Value Object / Repository interface / Domain Service |
| `demo-application` | Application | AppService (orchestration) / DTO / Converter |
| `demo-infrastructure` | Infrastructure | JPA impl / MQ / External services |
| `demo-interface` | Interface | REST Controller / Global exception handler |
| `demo-start` | Bootstrap | Startup class / Config / DB schema |

## API Reference API 接口

### Skill (Style 1 风格一：PO/Domain separation 分离)

| Method | Path | Description 说明 |
|--------|------|-------------------|
| POST | `/api/skills` | Create skill 创建 |
| GET | `/api/skills/{id}` | Get by ID 查询 |
| GET | `/api/skills` | Paged list 分页列表 |
| PUT | `/api/skills/{id}` | Update fields 更新详情 |
| PUT | `/api/skills/{id}/name` | Rename 修改名称 |
| PUT | `/api/skills/{id}/status` | Change status 状态变更 |
| POST | `/api/skills/{id}/activate` | Activate 启用 |
| POST | `/api/skills/{id}/archive` | Archive 归档 |
| POST | `/api/skills/batch-archive` | Batch archive 批量归档 |
| DELETE | `/api/skills/{id}` | Delete 删除 |

### Project

| Method | Path | Description 说明 |
|--------|------|-------------------|
| POST | `/api/projects` | Create project 创建 |
| GET | `/api/projects/{id}` | Get by ID 查询 |
| GET | `/api/projects` | List all 查询全部 |
| PUT | `/api/projects/{id}` | Update 更新 |
| POST | `/api/projects/{id}/activate` | Activate 激活 |
| POST | `/api/projects/{id}/complete` | Complete 完成 |
| DELETE | `/api/projects/{id}` | Delete 删除 |

### Order

| Method | Path | Description 说明 |
|--------|------|-------------------|
| POST | `/api/orders` | Create order 创建 |
| GET | `/api/orders/{id}` | Get by ID 查询 |
| POST | `/api/orders/{id}/pay` | Pay 支付 |
| DELETE | `/api/orders/{id}` | Delete 删除 |

## Repository Style Comparison 仓储风格对比

See details 详见: [docs/ddd-repository-styles.md](docs/ddd-repository-styles.md)

| | Style 1 风格一 (Skill) | Style 2 风格二 (Skill2) |
|---|---|---|
| Domain Model 领域模型 | Pure POJO 纯 POJO | @Entity on domain model |
| PO Class | Yes 有 | None 无 |
| Converter | Yes 有 (toDomain / toPO) | None 无 |
| Repository Impl | Handwritten class 手写 | JpaRepository auto-gen 自动生成 |
| Precise Update 精准更新 | Native SQL | JPQL @Modifying |
| Extra Files 额外文件 | 6 | 0 |

## DDD Practices DDD 实践

| Practice 实践 | Description 说明 |
|---------------|-------------------|
| Rich Domain 充血模型 | Behavior methods inside aggregate root 行为方法在聚合根内 |
| Factory Method 工厂方法 | `register()` / `place()` / `initiate()` instead of new + setter |
| reconstitute() | Rebuild from persistence, bypassing business constructor 从持久层重建 |
| Domain Event 领域事件 | Emitted on state change, published after transaction commit 状态变更后发布 |
| CQRS | Read repo returns lightweight projections; write repo operates aggregates |
| State Machine 状态机 | Enum + switch guards valid transition paths 校验合法路径 |
