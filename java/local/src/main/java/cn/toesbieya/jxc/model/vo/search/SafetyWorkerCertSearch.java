package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 安全管理人员证书管理搜索类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SafetyWorkerCertSearch extends BaseSearch {
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 持证人员姓名
     */
    private String holderName;
    
    /**
     * 身份证号码
     */
    private String idCard;
    
    /**
     * 取证类型
     */
    private String certType;
    
    /**
     * 发证单位
     */
    private String issuer;
    
    /**
     * 取证日期开始
     */
    private LocalDate issueDateStart;
    
    /**
     * 取证日期结束
     */
    private LocalDate issueDateEnd;
    
    /**
     * 复审日期开始
     */
    private LocalDate reviewDateStart;
    
    /**
     * 复审日期结束
     */
    private LocalDate reviewDateEnd;
    
    /**
     * 预警状态
     */
    private String alertStatus;
} 