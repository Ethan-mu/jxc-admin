package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 特种设备操作作业人员证书搜索参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialEquipmentWorkerCertSearch extends BaseSearch {
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 持证人员姓名
     */
    private String workerName;
    
    /**
     * 身份证号码
     */
    private String idCard;
    
    /**
     * 取证种类
     */
    private String certType;
    
    /**
     * 发证单位
     */
    private String issuer;
    
    /**
     * 取证时间开始
     */
    private LocalDate issueDateStart;
    
    /**
     * 取证时间结束
     */
    private LocalDate issueDateEnd;
    
    /**
     * 复审时间开始
     */
    private LocalDate reviewDateStart;
    
    /**
     * 复审时间结束
     */
    private LocalDate reviewDateEnd;
    
    /**
     * 预警状态
     */
    private String alertStatus;
    
    /**
     * 证书状态
     */
    private String status;
} 