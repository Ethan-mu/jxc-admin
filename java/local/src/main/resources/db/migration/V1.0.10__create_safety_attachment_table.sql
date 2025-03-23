CREATE TABLE biz_safety_attachment (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID，自增',
    uid INT NOT NULL COMMENT '用户ID，关联用户表',
    unit VARCHAR(50) NOT NULL COMMENT '单位',
    attach_type VARCHAR(100) NOT NULL COMMENT '附件类型',
    quantity INT NOT NULL COMMENT '数量',
    status ENUM('正常','损坏','待更换') COMMENT '使用状态',
    manager_name VARCHAR(255) COMMENT '设备管理人员姓名',
    manager_contact VARCHAR(20) COMMENT '设备管理人员联系方式',
    leader_name VARCHAR(255) COMMENT '主管领导姓名',
    leader_contact VARCHAR(20) COMMENT '主管领导联系方式',
    storage_location VARCHAR(255) COMMENT '储存位置',
    use_date DATE COMMENT '使用日期',
    recheck_date DATE COMMENT '复检日期',
    alert_status ENUM('未设置', '待预警', '已提醒', '已关闭') COMMENT '预警状态',
    alert_count INT NULL COMMENT '预警次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '安全附件管理表'; 