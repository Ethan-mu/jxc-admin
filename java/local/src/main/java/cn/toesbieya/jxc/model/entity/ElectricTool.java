package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 电动防护工器具实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("biz_electric_tools")
public class ElectricTool implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID，关联用户表
     */
    private Integer uid;

    /**
     * 证书名称
     */
    private String certificateName;

    /**
     * 工具类型
     */
    @NotBlank(message = "工具类型不能为空")
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
     * 使用状态
     */
    private String status;

    /**
     * 位置
     */
    private String location;

    /**
     * 设备管理人员姓名
     */
    private String managerName;

    /**
     * 设备管理人员联系方式
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
     * 预警状态
     */
    private String alertStatus;

    /**
     * 预警次数
     */
    private Integer alertCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 剩余天数（非数据库字段）
     */
    @TableField(exist = false)
    private Long remainingDays;

    /**
     * 检查状态（非数据库字段）
     */
    @TableField(exist = false)
    private String checkStatus;

    /**
     * 计算剩余天数
     */
    public void calculateRemainingDays() {
        if (this.recheckDate != null) {
            LocalDate now = LocalDate.now();
            this.remainingDays = ChronoUnit.DAYS.between(now, this.recheckDate);
            if (this.remainingDays < 0) {
                this.checkStatus = "已过期";
            } else if (this.remainingDays <= 30) {
                this.checkStatus = "即将过期";
            } else {
                this.checkStatus = "正常";
            }
        } else {
            this.remainingDays = null;
            this.checkStatus = "未设置复检日期";
        }
    }
} 