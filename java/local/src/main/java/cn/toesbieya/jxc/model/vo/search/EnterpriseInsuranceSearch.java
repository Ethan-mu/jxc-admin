package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 企业保险搜索条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnterpriseInsuranceSearch extends BaseSearch {
    
    /**
     * 保险种类
     */
    private String insuranceType;
    
    /**
     * 保险公司
     */
    private String company;
    
    /**
     * 负责人姓名
     */
    private String managerName;
    
    /**
     * 检查状态
     */
    private String checkStatus;
} 