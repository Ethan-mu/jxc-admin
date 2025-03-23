package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 应急物资管理导出VO
 */
@Data
public class EmergencySuppliesExport {
    @ExcelProperty(value = "ID", index = 0)
    @ColumnWidth(10)
    private Integer id;

    @ExcelProperty(value = "单位", index = 1)
    @ColumnWidth(15)
    private String unit;

    @ExcelProperty(value = "物资类型", index = 2)
    @ColumnWidth(25)
    private String supplyType;

    @ExcelProperty(value = "数量", index = 3)
    @ColumnWidth(10)
    private Integer quantity;

    @ExcelProperty(value = "使用状态", index = 4)
    @ColumnWidth(15)
    private String status;

    @ExcelProperty(value = "管理人员", index = 5)
    @ColumnWidth(15)
    private String managerName;

    @ExcelProperty(value = "管理人员联系方式", index = 6)
    @ColumnWidth(20)
    private String managerContact;

    @ExcelProperty(value = "主管领导", index = 7)
    @ColumnWidth(15)
    private String leaderName;

    @ExcelProperty(value = "主管领导联系方式", index = 8)
    @ColumnWidth(20)
    private String leaderContact;

    @ExcelProperty(value = "储存位置", index = 9)
    @ColumnWidth(30)
    private String storageLocation;

    @ExcelProperty(value = "使用日期", index = 10)
    @ColumnWidth(15)
    private String useDate;

    @ExcelProperty(value = "复检日期", index = 11)
    @ColumnWidth(15)
    private String recheckDate;

    @ExcelProperty(value = "剩余天数", index = 12)
    @ColumnWidth(10)
    private Long remainingDays;

    @ExcelProperty(value = "检查状态", index = 13)
    @ColumnWidth(10)
    private String checkStatus;

    @ExcelProperty(value = "预警状态", index = 14)
    @ColumnWidth(10)
    private String alertStatus;

    @ExcelProperty(value = "预警次数", index = 15)
    @ColumnWidth(10)
    private Integer alertCount;

    @ExcelProperty(value = "创建时间", index = 16)
    @ColumnWidth(20)
    private String createTime;
} 