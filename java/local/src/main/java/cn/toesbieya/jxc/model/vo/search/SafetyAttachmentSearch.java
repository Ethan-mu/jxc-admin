package cn.toesbieya.jxc.model.vo.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 安全附件管理搜索参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SafetyAttachmentSearch extends BaseSearch {
    
    /**
     * 附件名称
     */
    private String attachmentName;
    
    /**
     * 附件类型
     */
    private String attachmentType;
    
    /**
     * 文件名称
     */
    private String fileName;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 上传人
     */
    private String uploaderName;
    
    /**
     * 上传部门
     */
    private String uploaderDepartment;
    
    /**
     * 上传时间开始
     */
    private LocalDateTime uploadStartTime;
    
    /**
     * 上传时间结束
     */
    private LocalDateTime uploadEndTime;
    
    /**
     * 审核状态
     */
    private String approvalStatus;
    
    /**
     * 审核人
     */
    private String approverName;
    
    /**
     * 有效期开始日期
     */
    private LocalDate validFromStartDate;
    
    /**
     * 有效期开始日期结束
     */
    private LocalDate validFromEndDate;
    
    /**
     * 有效期结束日期开始
     */
    private LocalDate validToStartDate;
    
    /**
     * 有效期结束日期结束
     */
    private LocalDate validToEndDate;
    
    /**
     * 预警状态
     */
    private String alertStatus;
    
    /**
     * 相关标签
     */
    private String tags;

    /**
     * 单位
     */
    private String unit;

    /**
     * 使用状态：正常, 损坏, 待更换
     */
    private String status;

    /**
     * 检查状态：正常, 即将过期, 已过期
     */
    private String checkStatus;

    /**
     * 储存位置
     */
    private String storageLocation;

    /**
     * 管理人员
     */
    private String managerName;

    /**
     * 使用日期开始
     */
    private LocalDate useStartDate;

    /**
     * 使用日期结束
     */
    private LocalDate useEndDate;

    /**
     * 复检日期开始
     */
    private LocalDate recheckStartDate;

    /**
     * 复检日期结束
     */
    private LocalDate recheckEndDate;
} 