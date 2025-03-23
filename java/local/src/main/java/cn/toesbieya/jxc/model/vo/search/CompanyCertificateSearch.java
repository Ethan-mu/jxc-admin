package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 公司资质证书查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyCertificateSearch extends BaseSearch {
    
    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 颁证单位
     */
    private String issuingAuthority;
    
    /**
     * 管理人员姓名
     */
    private String managerName;
    
    /**
     * 预警状态
     */
    private String alertStatus;
    
    /**
     * 颁发开始时间
     */
    private LocalDate issueStartDate;
    
    /**
     * 颁发结束时间
     */
    private LocalDate issueEndDate;
    
    /**
     * 换证开始时间
     */
    private LocalDate renewalStartDate;
    
    /**
     * 换证结束时间
     */
    private LocalDate renewalEndDate;
} 