package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 特种作业人员证书实体类
 */
@Data
@TableName("biz_special_operation_worker_cert")
public class SpecialOperationWorkerCert {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 持证人员姓名
     */
    private String workerName;

    /**
     * 持证人员联系方式
     */
    private String workerContact;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 性别
     */
    private String gender;

    /**
     * 取证种类
     */
    private String certType;

    /**
     * 岗位
     */
    private String position;

    /**
     * 入职时间
     */
    private LocalDate hireDate;

    /**
     * 发证单位
     */
    private String issuer;

    /**
     * 主管领导姓名
     */
    private String leaderName;

    /**
     * 主管领导联系方式
     */
    private String leaderContact;

    /**
     * 操作证编码
     */
    private String operationCode;

    /**
     * 取证时间
     */
    private LocalDate issueDate;

    /**
     * 复审时间
     */
    private LocalDate reviewDate;

    /**
     * 证书预警状态
     */
    private String alertStatus;

    /**
     * 预警次数
     */
    private Integer alertCount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 剩余天数（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private Long remainingDays;

    /**
     * 证书状态（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String status;
} 