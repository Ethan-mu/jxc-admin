package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 安全管理人员证书管理实体类
 */
@Data
@TableName("biz_safety_worker_cert")
public class SafetyWorkerCert {
    
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
    private String holderName;

    /**
     * 持证人员手机
     */
    private String holderPhone;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 性别
     */
    private String gender;

    /**
     * 取证类型
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
     * 主管领导联系电话
     */
    private String leaderContact;

    /**
     * 操作证唯一编码
     */
    private String certCode;

    /**
     * 取证日期
     */
    private LocalDate issueDate;

    /**
     * 复审日期
     */
    private LocalDate reviewDate;

    /**
     * 证书预警状态
     */
    private String alertStatus;

    /**
     * 累计触发预警次数
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