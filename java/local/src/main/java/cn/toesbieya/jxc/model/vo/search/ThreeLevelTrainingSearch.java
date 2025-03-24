package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 三级教育培训搜索类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ThreeLevelTrainingSearch extends BaseSearch {
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 身份证号码
     */
    private String idNumber;
    
    /**
     * 现工种
     */
    private String currentJob;
    
    /**
     * 公司级培训时间范围开始
     */
    private LocalDate companyTrainDateStart;
    
    /**
     * 公司级培训时间范围结束
     */
    private LocalDate companyTrainDateEnd;
    
    /**
     * 车间级培训时间范围开始
     */
    private LocalDate workshopTrainDateStart;
    
    /**
     * 车间级培训时间范围结束
     */
    private LocalDate workshopTrainDateEnd;
    
    /**
     * 班组级培训时间范围开始
     */
    private LocalDate teamTrainDateStart;
    
    /**
     * 班组级培训时间范围结束
     */
    private LocalDate teamTrainDateEnd;
} 