-- 安全管理人员证书管理
CREATE TABLE biz_safety_worker_cert (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '唯一主键',
    uid INT NOT NULL COMMENT '关联用户ID',
    certificate_name VARCHAR(255) NOT NULL COMMENT '证书名称',
    holder_name VARCHAR(255) NOT NULL COMMENT '持证人员姓名',
    holder_phone VARCHAR(20) COMMENT '持证人员手机',
    id_card VARCHAR(18) NOT NULL COMMENT '身份证号码(18位)',
    gender ENUM('男','女') NOT NULL COMMENT '性别',
    cert_type VARCHAR(100) NOT NULL COMMENT '取证类型',
    position VARCHAR(100) COMMENT '岗位',
    hire_date DATE COMMENT '入职时间',
    issuer VARCHAR(255) COMMENT '发证单位',
    leader_name VARCHAR(255) COMMENT '主管领导姓名',
    leader_contact VARCHAR(20) COMMENT '主管领导联系电话',
    cert_code VARCHAR(50) UNIQUE COMMENT '操作证唯一编码',
    issue_date DATE NOT NULL COMMENT '取证日期',
    review_date DATE COMMENT '复审日期',
    alert_status ENUM('未设置', '待预警', '已提醒', '已关闭') DEFAULT '未设置' COMMENT '证书预警状态',
    alert_count INT DEFAULT 0 COMMENT '累计触发预警次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) COMMENT '安全管理人员持证信息档案表'; 