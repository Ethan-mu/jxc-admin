package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 电气保护工具实体类
 */
@Data
@TableName("biz_electrical_tool")
public class ElectricalTool {
    
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
     * 工具名称
     */
    private String toolName;
    
    /**
     * 工具类型
     */
    private String toolType;
    
    /**
     * 工具规格
     */
    private String specifications;
    
    /**
     * 工具编号
     */
    private String toolNumber;
    
    /**
     * 生产厂家
     */
    private String manufacturer;
    
    /**
     * 使用部门
     */
    private String department;
    
    /**
     * 存放位置
     */
    private String storageLocation;
    
    /**
     * 额定电压
     */
    private String ratedVoltage;
    
    /**
     * 绝缘等级
     */
    private String insulationLevel;
    
    /**
     * 使用状态
     */
    private String status;
    
    /**
     * 购买日期
     */
    private LocalDate purchaseDate;
    
    /**
     * 生产日期
     */
    private LocalDate manufactureDate;
    
    /**
     * 投入使用日期
     */
    private LocalDate useDate;
    
    /**
     * 上次检测日期
     */
    private LocalDate lastTestDate;
    
    /**
     * 下次检测日期
     */
    private LocalDate nextTestDate;
    
    /**
     * 检测周期（月）
     */
    private Integer testCycle;
    
    /**
     * 检测报告编号
     */
    private String testReportNumber;
    
    /**
     * 检测结果
     */
    private String testResult;
    
    /**
     * 检测机构
     */
    private String testInstitution;
    
    /**
     * 责任人
     */
    private String responsiblePerson;
    
    /**
     * 责任人联系方式
     */
    private String responsibleContact;
    
    /**
     * 剩余天数
     */
    private Long remainingDays;
    
    /**
     * 预警状态（未设置/待预警/已提醒/已关闭）
     */
    private String alertStatus;
    
    /**
     * 预警次数
     */
    private Integer alertCount;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 