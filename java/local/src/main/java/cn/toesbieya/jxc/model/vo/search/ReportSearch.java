package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 报告管理搜索类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportSearch extends BaseSearch {
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 报告类型
     */
    private String reportType;
    
    /**
     * 编制单位
     */
    private String compilingUnit;
    
    /**
     * 编制日期开始
     */
    private LocalDate compileDateStart;
    
    /**
     * 编制日期结束
     */
    private LocalDate compileDateEnd;
    
    /**
     * 备案/备查时间开始
     */
    private LocalDate recordDateStart;
    
    /**
     * 备案/备查时间结束
     */
    private LocalDate recordDateEnd;
    
    /**
     * 管理人员
     */
    private String managerName;
    
    /**
     * 预警状态
     */
    private String alertStatus;
} 