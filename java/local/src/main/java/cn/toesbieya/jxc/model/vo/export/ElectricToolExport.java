package cn.toesbieya.jxc.model.vo.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDate;

/**
 * 电动防护工器具导出实体类
 */
@Data
public class ElectricToolExport {
    /**
     * ID
     */
    @ExcelProperty(value = "ID", index = 0)
    @ColumnWidth(10)
    private Integer id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称", index = 1)
    @ColumnWidth(20)
    private String name;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型", index = 2)
    @ColumnWidth(20)
    private String type;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量", index = 3)
    @ColumnWidth(10)
    private Integer count;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", index = 4)
    @ColumnWidth(10)
    private String status;
    
    /**
     * 检查周期（天）
     */
    @ExcelProperty(value = "检查周期（天）", index = 5)
    @ColumnWidth(15)
    private Integer checkCycle;

    /**
     * 上次检查日期
     */
    @ExcelProperty(value = "上次检查日期", index = 6)
    @ColumnWidth(15)
    private String lastCheckTime;
    
    /**
     * 检查状态
     */
    @ExcelProperty(value = "检查状态", index = 7)
    @ColumnWidth(10)
    private String checkStatus;

    /**
     * 下次检查日期
     */
    @ExcelProperty(value = "下次检查日期", index = 8)
    @ColumnWidth(15)
    private String nextCheckTime;
    
    /**
     * 距下次检查天数
     */
    @ExcelProperty(value = "距下次检查天数", index = 9)
    @ColumnWidth(15)
    private String remainDaysStr;

    /**
     * 管理人员
     */
    @ExcelProperty(value = "管理人员", index = 10)
    @ColumnWidth(15)
    private String manager;

    /**
     * 存放位置
     */
    @ExcelProperty(value = "存放位置", index = 11)
    @ColumnWidth(20)
    private String location;
    
    /**
     * 预警状态
     */
    @ExcelProperty(value = "预警状态", index = 12)
    @ColumnWidth(10)
    private String alertStatus;
    
    /**
     * 预警次数
     */
    @ExcelProperty(value = "预警次数", index = 13)
    @ColumnWidth(10)
    private Integer alertCount;
    
    /**
     * 证书名称
     */
    private String certificateName;
    
    /**
     * 工具类型
     */
    private String toolType;
    
    /**
     * 规格
     */
    private String specifications;
    
    /**
     * 出厂编号
     */
    private String factoryNo;
    
    /**
     * 制造厂商
     */
    private String manufacturer;
    
    /**
     * 管理人员姓名
     */
    private String managerName;
    
    /**
     * 管理人员联系方式
     */
    private String managerContact;
    
    /**
     * 主管领导姓名
     */
    private String leaderName;
    
    /**
     * 主管领导联系方式
     */
    private String leaderContact;
    
    /**
     * 使用日期
     */
    private LocalDate useDate;
    
    /**
     * 复检日期
     */
    private LocalDate recheckDate;
    
    /**
     * 剩余天数
     */
    private Long remainingDays;
    
    /**
     * 创建时间
     */
    private String createTime;
} 