package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;

/**
 * 四新技术培训搜索参数
 */
@Data
public class FourNewTrainingSearch {
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 身份证号码
     */
    private String idNumber;
    
    /**
     * 现工种
     */
    private String currentJob;
    
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