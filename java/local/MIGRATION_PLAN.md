# 代码迁移计划

根据我们的代码风格统一要求，需要进行以下迁移操作：

## 1. Service实现类迁移

### 从 `cn.toesbieya.jxc.service.doc.impl` 迁移到 `cn.toesbieya.jxc.service.impl.doc`

- SafetyInspectionServiceImpl
- EnterpriseInsuranceServiceImpl
- ElectricToolServiceImpl
- SafetyAttachmentServiceImpl
- EmergencySuppliesServiceImpl
- SpecialEquipmentServiceImpl
- SpecialEquipmentWorkerCertServiceImpl
- SpecialOperationWorkerCertServiceImpl (如果存在)
- SafetyWorkerCertServiceImpl (如果存在)
- ElectricalToolServiceImpl (如果存在)
- ReportServiceImpl (如果存在)
- CompanyCertificateServiceImpl

### 步骤：

1. 创建目标目录（如果不存在）：`/Users/lzz/workPlace/jxc-admin/java/local/src/main/java/cn/toesbieya/jxc/service/impl/doc`
2. 移动文件到目标目录
3. 修改每个文件中的包声明从 `package cn.toesbieya.jxc.service.doc.impl;` 到 `package cn.toesbieya.jxc.service.impl.doc;`

## 2. Service接口命名统一

### 移除 `Biz` 前缀：

- 将 `BizPurchaseOrderService` 重命名为 `PurchaseOrderService`
- 将 `BizSellOrderService` 重命名为 `SellOrderService`
- 将 `BizSellOutboundService` 重命名为 `SellOutboundService`
- 将 `BizPurchaseInboundService` 重命名为 `PurchaseInboundService`
- 将 `BizDocHistoryService` 重命名为 `DocHistoryService`

### 步骤：

1. 创建新的接口文件，使用新的命名规则
2. 更新所有使用这些接口的类（实现类、Controller等）

## 3. 实体类命名统一

### 移除 `Biz` 前缀：

- 将 `BizSellOrder` 重命名为 `SellOrder`
- 将 `BizPurchaseOrder` 重命名为 `PurchaseOrder`
- 将 `BizSellOutbound` 重命名为 `SellOutbound`
- 将 `BizPurchaseInbound` 重命名为 `PurchaseInbound`
- 将 `BizDocHistory` 重命名为 `DocHistory`
- 将 `BizStock` 重命名为 `Stock`

### 步骤：

1. 创建新的实体类文件，使用新的命名规则
2. 更新所有使用这些实体类的代码

## 4. Controller命名和路径统一

1. 确保所有Controller类都放在正确的子包中
2. 确保所有Controller类都使用正确的命名规则（与对应的Service接口名称对应）

## 5. 统一代码风格

1. 使用Lombok注解简化代码
2. 使用`@Slf4j`统一日志处理
3. 统一依赖注入方式，优先使用构造器注入或`@Resource`
4. 统一方法命名

## 执行计划

为了安全起见，建议按照以下顺序进行代码迁移：

1. 先备份所有代码
2. 创建一个新的分支进行迁移工作
3. 按模块进行迁移，每个模块迁移后进行编译和测试
4. 从较小的模块开始迁移
5. 完成全部迁移后，进行全面的测试，确保所有功能正常工作
6. 合并分支到主线

## 迁移后检查清单

- [ ] 所有Service实现类都在正确的包下
- [ ] 所有Service接口都使用统一的命名规则
- [ ] 所有实体类都使用统一的命名规则
- [ ] 所有Controller类都在正确的包下
- [ ] 所有Controller类都使用正确的命名规则
- [ ] 代码能够正常编译
- [ ] 所有功能都正常工作 