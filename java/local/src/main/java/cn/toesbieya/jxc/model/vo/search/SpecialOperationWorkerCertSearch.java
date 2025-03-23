package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 特种作业人员证书搜索参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialOperationWorkerCertSearch extends BaseSearch {
    
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
     * 取证日期开始
     */
    private String issueDateStart;
    
    /**
     * 取证日期结束
     */
    private String issueDateEnd;
    
    /**
     * 复审日期开始
     */
    private String reviewDateStart;
    
    /**
     * 复审日期结束
     */
    private String reviewDateEnd;
    
    /**
     * 预警状态
     */
    private String alertStatus;
    
    /**
     * 证书状态
     */
    private String status;
} 