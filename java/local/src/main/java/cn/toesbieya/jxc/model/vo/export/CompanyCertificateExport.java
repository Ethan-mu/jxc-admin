package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class CompanyCertificateExport {
    @ExcelProperty(value = "证书ID", index = 0)
    @ColumnWidth(10)
    private Integer id;

    @ExcelProperty(value = "证书名称", index = 1)
    @ColumnWidth(30)
    private String certificateName;

    @ExcelProperty(value = "颁证单位", index = 2)
    @ColumnWidth(30)
    private String issuingAuthority;

    @ExcelProperty(value = "颁发日期", index = 3)
    @ColumnWidth(15)
    private String issueDate;

    @ExcelProperty(value = "换证时间", index = 4)
    @ColumnWidth(15)
    private String renewalDate;

    @ExcelProperty(value = "复证时间", index = 5)
    @ColumnWidth(15)
    private String reviewDate;

    @ExcelProperty(value = "剩余天数", index = 6)
    @ColumnWidth(10)
    private Long remainingDays;

    @ExcelProperty(value = "存放位置", index = 7)
    @ColumnWidth(30)
    private String storageLocation;

    @ExcelProperty(value = "管理人员", index = 8)
    @ColumnWidth(15)
    private String managerName;

    @ExcelProperty(value = "管理人员联系方式", index = 9)
    @ColumnWidth(20)
    private String managerContact;

    @ExcelProperty(value = "主管领导", index = 10)
    @ColumnWidth(15)
    private String leaderName;

    @ExcelProperty(value = "主管领导联系方式", index = 11)
    @ColumnWidth(20)
    private String leaderContact;

    @ExcelProperty(value = "预警状态", index = 12)
    @ColumnWidth(10)
    private String alertStatus;

    @ExcelProperty(value = "预警次数", index = 13)
    @ColumnWidth(10)
    private Integer alertCount;

    @ExcelProperty(value = "创建时间", index = 14)
    @ColumnWidth(20)
    private String createTime;
} 