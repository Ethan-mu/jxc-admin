package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 电动防护工器具查询对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElectricToolSearch extends BaseSearch {
    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 检查状态
     */
    private String checkStatus;
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 工具类型
     */
    private String toolType;
    
    /**
     * 规格
     */
    private String specifications;
    
    /**
     * 出厂编号
     */
    private String factoryNo;
    
    /**
     * 制造厂商
     */
    private String manufacturer;
    
    /**
     * 位置
     */
    private String location;
    
    /**
     * 管理人员
     */
    private String managerName;
    
    /**
     * 使用日期开始
     */
    private LocalDate useStartDate;
    
    /**
     * 使用日期结束
     */
    private LocalDate useEndDate;
    
    /**
     * 复检日期开始
     */
    private LocalDate recheckStartDate;
    
    /**
     * 复检日期结束
     */
    private LocalDate recheckEndDate;
    
    /**
     * 预警状态
     */
    private String alertStatus;
} 