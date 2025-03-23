package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 特种设备管理导出类
 */
@Data
public class SpecialEquipmentExport {
    
    @ExcelProperty(value = "设备ID", index = 0)
    @ColumnWidth(10)
    private Integer id;
    
    @ExcelProperty(value = "设备名称", index = 1)
    @ColumnWidth(30)
    private String certificateName;
    
    @ExcelProperty(value = "设备类型", index = 2)
    @ColumnWidth(15)
    private String equipmentType;
    
    @ExcelProperty(value = "规格", index = 3)
    @ColumnWidth(15)
    private String specifications;
    
    @ExcelProperty(value = "出厂编号", index = 4)
    @ColumnWidth(20)
    private String factoryNo;
    
    @ExcelProperty(value = "注册号", index = 5)
    @ColumnWidth(20)
    private String registerNo;
    
    @ExcelProperty(value = "使用证", index = 6)
    @ColumnWidth(20)
    private String usageCert;
    
    @ExcelProperty(value = "管理人员", index = 7)
    @ColumnWidth(15)
    private String managerName;
    
    @ExcelProperty(value = "管理人员联系方式", index = 8)
    @ColumnWidth(20)
    private String managerContact;
    
    @ExcelProperty(value = "主管领导", index = 9)
    @ColumnWidth(15)
    private String leaderName;
    
    @ExcelProperty(value = "主管领导联系方式", index = 10)
    @ColumnWidth(20)
    private String leaderContact;
    
    @ExcelProperty(value = "制造商", index = 11)
    @ColumnWidth(30)
    private String manufacturer;
    
    @ExcelProperty(value = "设备状态", index = 12)
    @ColumnWidth(10)
    private String status;
    
    @ExcelProperty(value = "安装位置", index = 13)
    @ColumnWidth(20)
    private String installLocation;
    
    @ExcelProperty(value = "使用日期", index = 14)
    @ColumnWidth(15)
    private String useDate;
    
    @ExcelProperty(value = "首检日期", index = 15)
    @ColumnWidth(15)
    private String firstCheck;
    
    @ExcelProperty(value = "上次检查日期", index = 16)
    @ColumnWidth(15)
    private String lastCheck;
    
    @ExcelProperty(value = "复检日期", index = 17)
    @ColumnWidth(15)
    private String recheckDate;
    
    @ExcelProperty(value = "剩余天数", index = 18)
    @ColumnWidth(10)
    private Long remainingDays;
    
    @ExcelProperty(value = "预警状态", index = 19)
    @ColumnWidth(10)
    private String alertStatus;
    
    @ExcelProperty(value = "预警次数", index = 20)
    @ColumnWidth(10)
    private Integer alertCount;
    
    @ExcelProperty(value = "创建时间", index = 21)
    @ColumnWidth(20)
    private String createTime;
} 