CREATE TABLE `biz_company_certificates` (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一标识每条记录',
    uid INT NULL COMMENT '用户id',
    certificate_name VARCHAR(255) NOT NULL COMMENT '资质证书的名称',
    issuing_authority VARCHAR(255) NOT NULL COMMENT '颁证单位的名称',
    issue_date DATE NOT NULL COMMENT '证书的颁发时间',
    renewal_date DATE COMMENT '证书的换证时间',
    review_date DATE COMMENT '证书的复证时间',
    storage_location VARCHAR(255) COMMENT '证书的存放位置',
    manager_name VARCHAR(255) COMMENT '管理人员的姓名',
    manager_contact VARCHAR(20) COMMENT '管理人员的联系方式',
    leader_name VARCHAR(255) COMMENT '企业主管领导的姓名',
    leader_contact VARCHAR(20) COMMENT '企业主管领导的联系方式',
    alert_status ENUM('未设置', '待预警', '已提醒', '已关闭') DEFAULT '未设置' COMMENT '预警状态，表示证书的当前预警情况',
    alert_count INT DEFAULT 0 COMMENT '预警状态次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='企业资质证书管理表'; 