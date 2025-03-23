package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 电气保护工具管理导出类
 */
@Data
public class ElectricalToolExport {
    
    @ExcelProperty(value = "工具ID", index = 0)
    @ColumnWidth(10)
    private Integer id;
    
    @ExcelProperty(value = "工具名称", index = 1)
    @ColumnWidth(25)
    private String toolName;
    
    @ExcelProperty(value = "工具类型", index = 2)
    @ColumnWidth(15)
    private String toolType;
    
    @ExcelProperty(value = "规格型号", index = 3)
    @ColumnWidth(20)
    private String specifications;
    
    @ExcelProperty(value = "工具编号", index = 4)
    @ColumnWidth(20)
    private String toolNumber;
    
    @ExcelProperty(value = "生产厂家", index = 5)
    @ColumnWidth(25)
    private String manufacturer;
    
    @ExcelProperty(value = "使用部门", index = 6)
    @ColumnWidth(15)
    private String department;
    
    @ExcelProperty(value = "存放位置", index = 7)
    @ColumnWidth(20)
    private String storageLocation;
    
    @ExcelProperty(value = "额定电压(V)", index = 8)
    @ColumnWidth(12)
    private Integer ratedVoltage;
    
    @ExcelProperty(value = "绝缘等级", index = 9)
    @ColumnWidth(10)
    private String insulationLevel;
    
    @ExcelProperty(value = "工具状态", index = 10)
    @ColumnWidth(10)
    private String status;
    
    @ExcelProperty(value = "购买日期", index = 11)
    @ColumnWidth(15)
    private String purchaseDate;
    
    @ExcelProperty(value = "出厂日期", index = 12)
    @ColumnWidth(15)
    private String manufactureDate;
    
    @ExcelProperty(value = "投入使用日期", index = 13)
    @ColumnWidth(15)
    private String useDate;
    
    @ExcelProperty(value = "上次检测日期", index = 14)
    @ColumnWidth(15)
    private String lastTestDate;
    
    @ExcelProperty(value = "下次检测日期", index = 15)
    @ColumnWidth(15)
    private String nextTestDate;
    
    @ExcelProperty(value = "检测周期(月)", index = 16)
    @ColumnWidth(12)
    private Integer testCycle;
    
    @ExcelProperty(value = "检测报告编号", index = 17)
    @ColumnWidth(20)
    private String testReportNumber;
    
    @ExcelProperty(value = "检测结果", index = 18)
    @ColumnWidth(10)
    private String testResult;
    
    @ExcelProperty(value = "检测机构", index = 19)
    @ColumnWidth(25)
    private String testInstitution;
    
    @ExcelProperty(value = "责任人", index = 20)
    @ColumnWidth(15)
    private String responsiblePerson;
    
    @ExcelProperty(value = "责任人联系方式", index = 21)
    @ColumnWidth(20)
    private String responsibleContact;
    
    @ExcelProperty(value = "剩余天数", index = 22)
    @ColumnWidth(10)
    private Long remainingDays;
    
    @ExcelProperty(value = "预警状态", index = 23)
    @ColumnWidth(10)
    private String alertStatus;
    
    @ExcelProperty(value = "预警次数", index = 24)
    @ColumnWidth(10)
    private Integer alertCount;
    
    @ExcelProperty(value = "备注", index = 25)
    @ColumnWidth(30)
    private String remark;
    
    @ExcelProperty(value = "创建时间", index = 26)
    @ColumnWidth(20)
    private String createTime;
} 