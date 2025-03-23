package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 特种设备搜索参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialEquipmentSearch extends BaseSearch {
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 设备类型
     */
    private String equipmentType;
    
    /**
     * 出厂编号
     */
    private String factoryNo;
    
    /**
     * 注册编号
     */
    private String registerNo;
    
    /**
     * 设备管理人员姓名
     */
    private String managerName;
    
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
     * 校验状态
     */
    private String checkStatus;
} 