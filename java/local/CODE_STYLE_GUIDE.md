# 代码风格和结构统一指南

为了保持项目的一致性和可维护性，我们建议遵循以下代码风格和结构标准。

## 包结构标准

### 1. Controller层

所有Controller类应该放在适当的子包中：

- 基本Controller: `cn.toesbieya.jxc.controller`
- 系统管理相关: `cn.toesbieya.jxc.controller.sys`
- 消息相关: `cn.toesbieya.jxc.controller.msg`
- 文档相关: `cn.toesbieya.jxc.controller.doc`

### 2. Service层

所有Service接口应该放在适当的子包中：

- 基本Service接口: `cn.toesbieya.jxc.service`
- 系统管理相关: `cn.toesbieya.jxc.service.sys`
- 消息相关: `cn.toesbieya.jxc.service.msg`
- 文档相关: `cn.toesbieya.jxc.service.doc`

### 3. Service实现层

所有Service实现类应该放在与接口对应的impl子包中：

- 基本Service实现: `cn.toesbieya.jxc.service.impl`
- 系统管理相关: `cn.toesbieya.jxc.service.impl.sys`
- 消息相关: `cn.toesbieya.jxc.service.impl.msg`
- 文档相关: `cn.toesbieya.jxc.service.impl.doc`

### 4. Mapper层

所有Mapper接口应该直接放在mapper包下：

- `cn.toesbieya.jxc.mapper`

### 5. 实体类

所有实体类应该放在model的entity子包中：

- `cn.toesbieya.jxc.model.entity`

### 6. VO对象

所有VO对象按照其用途放在适当的子包中：

- 基本VO: `cn.toesbieya.jxc.model.vo`
- 搜索VO: `cn.toesbieya.jxc.model.vo.search`
- 导出VO: `cn.toesbieya.jxc.model.vo.export`
- 更新VO: `cn.toesbieya.jxc.model.vo.update`
- 结果VO: `cn.toesbieya.jxc.model.vo.result`

## 命名规范

### 1. Controller

- 类名后缀为`Controller`
- 名称应当与对应的Service接口保持一致（不包括后缀）
- 例如：`SafetyInspectionService` -> `SafetyInspectionController`

### 2. Service接口

- 类名后缀为`Service`
- 不应使用`Biz`等前缀，除非表示特定的业务逻辑分组
- 例如：`SafetyInspectionService`而不是`BizSafetyInspectionService`

### 3. Service实现类

- 类名后缀为`ServiceImpl`
- 应当与Service接口名称对应
- 例如：`SafetyInspectionService` -> `SafetyInspectionServiceImpl`

### 4. Mapper接口

- 类名后缀为`Mapper`
- 例如：`SafetyInspectionMapper`

### 5. 实体类

- 应直接使用业务名称，不使用额外前缀
- 例如：`SafetyInspection`而不是`BizSafetyInspection`

## 代码风格

### 1. 注解使用

- 使用Lombok简化代码
- Controller类使用`@RestController`和`@RequestMapping`
- Service实现类使用`@Service`
- 事务方法使用`@Transactional`

### 2. 日志使用

- 优先使用Lombok的`@Slf4j`注解
- 不要使用System.out进行日志打印

### 3. 依赖注入

- 优先使用构造器注入或`@Resource`
- 避免使用`@Autowired`直接注入字段

### 4. 方法命名

- 增删改查方法名称保持统一：add, delete, update, get, page等
- 导出方法统一命名为`export`
- 批量操作方法应该添加`batch`前缀，如`batchDelete`

## 示例

以下是一个标准的Service接口和实现类示例：

```java
// Service接口
package cn.toesbieya.jxc.service.doc;

public interface SafetyInspectionService {
    R page(SafetyInspectionSearch search);
    R add(SafetyInspection inspection);
    R update(SafetyInspection inspection);
    void export(SafetyInspectionSearch search, HttpServletResponse response);
}

// Service实现类
package cn.toesbieya.jxc.service.impl.doc;

@Service
@Slf4j
public class SafetyInspectionServiceImpl extends ServiceImpl<SafetyInspectionMapper, SafetyInspection> implements SafetyInspectionService {
    // 实现方法
}
``` 