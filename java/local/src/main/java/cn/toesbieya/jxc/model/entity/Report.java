package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 报告管理实体类
 */
@Data
@TableName("biz_report")
public class Report {
    
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
     * 报告类型
     */
    private String reportType;

    /**
     * 编制单位
     */
    private String compilingUnit;

    /**
     * 编制日期
     */
    private LocalDate compileDate;

    /**
     * 备案/备查时间
     */
    private LocalDate recordDate;

    /**
     * 下次备案/备查时间
     */
    private LocalDate nextRecordDate;

    /**
     * 存放位置
     */
    private String storageLocation;

    /**
     * 管理人员姓名
     */
    private String managerName;

    /**
     * 管理人员联系方式
     */
    private String managerContact;

    /**
     * 主管领导姓名
     */
    private String leaderName;

    /**
     * 主管领导联系方式
     */
    private String leaderContact;

    /**
     * 预警状态（未设置、待预警、已提醒、已关闭）
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
} 