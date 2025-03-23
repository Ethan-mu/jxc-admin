package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 特种设备实体类
 */
@Data
@TableName("biz_special_equipment")
public class SpecialEquipment {
    
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
     * 设备类型
     */
    private String equipmentType;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 出厂编号
     */
    private String factoryNo;

    /**
     * 注册编号
     */
    private String registerNo;

    /**
     * 使用证书
     */
    private String usageCert;

    /**
     * 设备管理人员姓名
     */
    private String managerName;

    /**
     * 设备管理人员联系方式
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
     * 制造厂家
     */
    private String manufacturer;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 安装位置
     */
    private String installLocation;

    /**
     * 使用日期
     */
    private LocalDate useDate;

    /**
     * 初次校验时间
     */
    private LocalDate firstCheck;

    /**
     * 最近一次校验时间
     */
    private LocalDate lastCheck;

    /**
     * 复检日期
     */
    private LocalDate recheckDate;

    /**
     * 预警状态
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
     * 校验状态（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String checkStatus;
} 