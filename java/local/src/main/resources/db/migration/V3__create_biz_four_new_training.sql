-- 创建四新技术培训表
DROP TABLE IF EXISTS `biz_four_new_training`;
CREATE TABLE `biz_four_new_training` (
    id                bigint auto_increment comment '主键ID'
        primary key,
    uid               int                                not null comment '用户id',
    employee_name     varchar(30)                         null comment '员工姓名',
    gender            varchar(2)                          null comment '性别',
    age               tinyint unsigned                    null comment '年龄',
    phone             char(11)                            null comment '手机号码',
    original_job      varchar(50)                         null comment '原工种',
    current_job       varchar(50)                         null comment '现工种',
    join_date         date                                null comment '入职日期',
    id_number         char(18)                            null comment '身份证号码',
    training_date     date                                null comment '培训日期',
    training_location varchar(100)                        null comment '培训地点',
    new_technology    text                                null comment '新工艺内容',
    new_equipment     text                                null comment '新设备操作要点',
    new_technique     text                                null comment '新技术规范',
    new_material      text                                null comment '新材料特性',
    lecturer          varchar(50)                         null comment '主讲人',
    main_content      text                                null comment '主讲内容',
    score             varchar(20)                         null comment '考核成绩',
    created_time      timestamp default CURRENT_TIMESTAMP null comment '记录创建时间',
    updated_time      timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后更新时间',
    INDEX idx_employee_name (employee_name),
    INDEX idx_id_number (id_number),
    INDEX idx_current_job (current_job),
    INDEX idx_training_date (training_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工四新技术培训记录表'; 