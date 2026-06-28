# Java DDD 项目编码规范

## 代码风格

### 链式调用不换行
短链式调用保持在一行，不要无意义断行：

只有以下情况允许断行：
- lambda 体本身复杂（如含大量转义字符的字符串拼接）

### Entity 必须加 @DynamicUpdate
所有 `*PO` 类标注 `@DynamicUpdate`，避免全字段 UPDATE 导致并发覆盖风险。只更新脏字段，生成的 SQL 仅变动的列。
