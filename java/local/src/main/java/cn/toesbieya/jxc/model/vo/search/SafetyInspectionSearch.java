package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 安全隐患排查搜索条件类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SafetyInspectionSearch extends BaseSearch {

    /**
     * 检查人员
     */
    private String inspector;

    /**
     * 隐患等级
     */
    private String hazardLevel;

    /**
     * 整改负责人
     */
    private String responsiblePerson;

    /**
     * 公示情况
     */
    private String publicityStatus;

    /**
     * 上报情况
     */
    private String reportStatus;

    /**
     * 是否已经整改
     */
    private Integer isResolved;

    /**
     * 预警状态
     */
    private String alertStatus;

    /**
     * 整改期限开始日期
     */
    private LocalDate deadlineBegin;

    /**
     * 整改期限结束日期
     */
    private LocalDate deadlineEnd;

    /**
     * 隐患整改开始时间
     */
    private LocalDate rectifyDateBegin;

    /**
     * 隐患整改结束时间
     */
    private LocalDate rectifyDateEnd;

    /**
     * 验证开始时间
     */
    private LocalDate verifyDateBegin;

    /**
     * 验证结束时间
     */
    private LocalDate verifyDateEnd;
} 