package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应急物资管理实体类
 */
@Data
@TableName("biz_emergency_supplies")
public class EmergencySupplies {
    
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
     * 单位
     */
    private String unit;

    /**
     * 物资类型
     */
    private String supplyType;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 使用状态（可用、已领用、维修中）
     */
    private String status;

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
     * 储存位置
     */
    private String storageLocation;

    /**
     * 使用日期
     */
    private LocalDate useDate;

    /**
     * 复检日期
     */
    private LocalDate recheckDate;

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
    
    /**
     * 检查状态（非数据库字段，用于前端展示）
     */
    @TableField(exist = false)
    private String checkStatus;
} 