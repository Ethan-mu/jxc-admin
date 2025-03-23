package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 企业保险导出实体类
 */
@Data
public class EnterpriseInsuranceExport {
    
    @ExcelProperty(value = "ID", index = 0)
    @ColumnWidth(10)
    private Integer id;
    
    @ExcelProperty(value = "保险种类", index = 1)
    @ColumnWidth(20)
    private String insuranceType;
    
    @ExcelProperty(value = "保险公司", index = 2)
    @ColumnWidth(20)
    private String company;
    
    @ExcelProperty(value = "保险金额", index = 3)
    @ColumnWidth(15)
    private BigDecimal amount;
    
    @ExcelProperty(value = "参保人数", index = 4)
    @ColumnWidth(10)
    private Integer insuredCount;
    
    @ExcelProperty(value = "保险日期", index = 5)
    @ColumnWidth(15)
    private String startDate;
    
    @ExcelProperty(value = "到期日期", index = 6)
    @ColumnWidth(15)
    private String endDate;
    
    @ExcelProperty(value = "剩余天数", index = 7)
    @ColumnWidth(10)
    private String remainingDaysStr;
    
    @ExcelProperty(value = "检查状态", index = 8)
    @ColumnWidth(10)
    private String checkStatus;
    
    @ExcelProperty(value = "负责人姓名", index = 9)
    @ColumnWidth(15)
    private String managerName;
    
    @ExcelProperty(value = "负责人电话", index = 10)
    @ColumnWidth(15)
    private String managerPhone;
    
    @ExcelProperty(value = "预警状态", index = 11)
    @ColumnWidth(10)
    private String alertStatus;
    
    @ExcelProperty(value = "预警次数", index = 12)
    @ColumnWidth(10)
    private Integer alertCount;
    
    @ExcelProperty(value = "创建时间", index = 13)
    @ColumnWidth(20)
    private String createTime;
} 