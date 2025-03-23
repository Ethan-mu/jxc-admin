package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 报告导出实体类
 */
@Data
public class ReportExport {
    
    @ExcelProperty(value = "报告ID", index = 0)
    @ColumnWidth(10)
    private Integer id;
    
    @ExcelProperty(value = "证书名称", index = 1)
    @ColumnWidth(30)
    private String certificateName;
    
    @ExcelProperty(value = "报告类型", index = 2)
    @ColumnWidth(20)
    private String reportType;
    
    @ExcelProperty(value = "编制单位", index = 3)
    @ColumnWidth(30)
    private String compilingUnit;
    
    @ExcelProperty(value = "编制日期", index = 4)
    @ColumnWidth(15)
    private String compileDate;
    
    @ExcelProperty(value = "备案/备查时间", index = 5)
    @ColumnWidth(15)
    private String recordDate;
    
    @ExcelProperty(value = "下次备案/备查时间", index = 6)
    @ColumnWidth(20)
    private String nextRecordDate;
    
    @ExcelProperty(value = "剩余天数", index = 7)
    @ColumnWidth(10)
    private Long remainingDays;
    
    @ExcelProperty(value = "存放位置", index = 8)
    @ColumnWidth(30)
    private String storageLocation;
    
    @ExcelProperty(value = "管理人员", index = 9)
    @ColumnWidth(15)
    private String managerName;
    
    @ExcelProperty(value = "管理人员联系方式", index = 10)
    @ColumnWidth(20)
    private String managerContact;
    
    @ExcelProperty(value = "主管领导", index = 11)
    @ColumnWidth(15)
    private String leaderName;
    
    @ExcelProperty(value = "主管领导联系方式", index = 12)
    @ColumnWidth(20)
    private String leaderContact;
    
    @ExcelProperty(value = "预警状态", index = 13)
    @ColumnWidth(10)
    private String alertStatus;
    
    @ExcelProperty(value = "预警次数", index = 14)
    @ColumnWidth(10)
    private Integer alertCount;
    
    @ExcelProperty(value = "创建时间", index = 15)
    @ColumnWidth(20)
    private String createTime;
} 