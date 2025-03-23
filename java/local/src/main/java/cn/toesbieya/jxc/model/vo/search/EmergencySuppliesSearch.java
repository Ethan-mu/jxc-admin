package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 应急物资查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmergencySuppliesSearch extends BaseSearch {
    
    /**
     * 物资类型
     */
    private String supplyType;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 使用状态
     */
    private String status;
    
    /**
     * 储存位置
     */
    private String storageLocation;
    
    /**
     * 管理人员姓名
     */
    private String managerName;
    
    /**
     * 使用日期开始
     */
    private LocalDate useDateStart;
    
    /**
     * 使用日期结束
     */
    private LocalDate useDateEnd;
    
    /**
     * 复检日期开始
     */
    private LocalDate recheckDateStart;
    
    /**
     * 复检日期结束
     */
    private LocalDate recheckDateEnd;
    
    /**
     * 预警状态
     */
    private String alertStatus;
    
    /**
     * 检查状态
     */
    private String checkStatus;
} 