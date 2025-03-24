-- 创建转岗教育培训表
DROP TABLE IF EXISTS `biz_return_job_training`;
CREATE TABLE `biz_return_job_training` (
    id                bigint auto_increment comment '主键ID'
        primary key,
    uid               int                                not null comment '用户id',
    name              varchar(50)                         null comment '姓名',
    gender            varchar(2)                          null comment '性别',
    age               int                                 null comment '年龄',
    phone             varchar(11)                         null comment '联系电话',
    original_job      varchar(50)                         null comment '原工种',
    current_job       varchar(50)                         null comment '现工种',
    join_date         date                                null comment '入职日期',
    id_number         varchar(18)                         null comment '身份证号码（唯一）',
    original_post     varchar(50)                         null comment '原岗位',
    current_post      varchar(50)                         null comment '现岗位',
    training_level    varchar(20)                         null comment '培训级别（公司级/车间级/班组级）',
    training_date     date                                null comment '培训时间',
    training_location varchar(100)                        null comment '培训地点',
    lecturer          varchar(50)                         null comment '授课人',
    main_content      text                                null comment '主讲内容',
    score             varchar(20)                         null comment '考核成绩',
    created_time      timestamp default CURRENT_TIMESTAMP null comment '记录创建时间',
    updated_time      timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后更新时间',
    INDEX idx_name (name),
    INDEX idx_id_number (id_number),
    INDEX idx_current_job (current_job),
    INDEX idx_training_level (training_level),
    INDEX idx_training_date (training_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='转岗教育培训'; 