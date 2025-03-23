package cn.toesbieya.jxc.model.vo.export;

import cn.toesbieya.jxc.model.entity.SafetyInspection;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 安全隐患排查导出实体类
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SafetyInspectionExport {

    @ExcelProperty("检查人员")
    private String inspector;

    @ExcelProperty("隐患描述")
    private String hazardDesc;

    @ExcelProperty("隐患等级")
    private String hazardLevel;

    @ExcelProperty("整改措施")
    private String correctiveAction;

    @ExcelProperty("整改期限")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate deadline;

    @ExcelProperty("检查负责人电话")
    private String inspectorContact;

    @ExcelProperty("整改资金来源")
    private String fundSource;

    @ExcelProperty("整改负责人")
    private String responsiblePerson;

    @ExcelProperty("整改负责人电话")
    private String respPersonPhone;

    @ExcelProperty("隐患整改时间")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate rectifyDate;

    @ExcelProperty("验收负责人")
    private String verifier;

    @ExcelProperty("验证时间")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDate verifyDate;

    @ExcelProperty("公示情况")
    private String publicityStatus;

    @ExcelProperty("上报情况")
    private String reportStatus;

    @ExcelProperty("是否已整改")
    private String isResolvedStr;

    @ExcelProperty("预警状态")
    private String alertStatus;

    @ExcelProperty("预警次数")
    private Integer alertCount;

    public SafetyInspectionExport(SafetyInspection inspection) {
        this.inspector = inspection.getInspector();
        this.hazardDesc = inspection.getHazardDesc();
        this.hazardLevel = inspection.getHazardLevel();
        this.correctiveAction = inspection.getCorrectiveAction();
        this.deadline = inspection.getDeadline();
        this.inspectorContact = inspection.getInspectorContact();
        this.fundSource = inspection.getFundSource();
        this.responsiblePerson = inspection.getResponsiblePerson();
        this.respPersonPhone = inspection.getRespPersonPhone();
        this.rectifyDate = inspection.getRectifyDate();
        this.verifier = inspection.getVerifier();
        this.verifyDate = inspection.getVerifyDate();
        this.publicityStatus = inspection.getPublicityStatus();
        this.reportStatus = inspection.getReportStatus();
        this.isResolvedStr = inspection.getIsResolved() != null && inspection.getIsResolved() == 1 ? "已整改" : "未整改";
        this.alertStatus = inspection.getAlertStatus();
        this.alertCount = inspection.getAlertCount();
    }
} 