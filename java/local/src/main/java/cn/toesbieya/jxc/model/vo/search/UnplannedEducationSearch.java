package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 计划外培训查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UnplannedEducationSearch extends BaseSearch {
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 身份证号
     */
    private String idNumber;
    
    /**
     * 当前岗位
     */
    private String currentJob;
    
    /**
     * 培训起始日期
     */
    private String startDate;
    
    /**
     * 培训结束日期
     */
    private String endDate;
} 