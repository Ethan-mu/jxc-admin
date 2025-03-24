package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;

/**
 * 复岗教育培训搜索参数
 */
@Data
public class ReturnJobTrainingSearch {
    
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
     * 培训级别
     */
    private String trainingLevel;
    
    /**
     * 培训日期开始
     */
    private String trainingDateStart;
    
    /**
     * 培训日期结束
     */
    private String trainingDateEnd;
    
    /**
     * 页码
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
} 