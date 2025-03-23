package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 电气保护工具管理搜索类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElectricalToolSearch extends BaseSearch {
    
    /**
     * 工具名称
     */
    private String toolName;
    
    /**
     * 工具类型
     */
    private String toolType;
    
    /**
     * 工具编号
     */
    private String toolNumber;
    
    /**
     * 规格型号
     */
    private String specifications;
    
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
     * 绝缘等级
     */
    private String insulationLevel;
    
    /**
     * 工具状态
     */
    private String status;
    
    /**
     * 责任人
     */
    private String responsiblePerson;
    
    /**
     * 购买日期开始
     */
    private LocalDate purchaseStartDate;
    
    /**
     * 购买日期结束
     */
    private LocalDate purchaseEndDate;
    
    /**
     * 下次检测日期开始
     */
    private LocalDate nextTestStartDate;
    
    /**
     * 下次检测日期结束
     */
    private LocalDate nextTestEndDate;
    
    /**
     * 检测结果
     */
    private String testResult;
    
    /**
     * 预警状态
     */
    private String alertStatus;
} 