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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 企业保险实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("biz_enterprise_insurance")
public class EnterpriseInsurance implements Serializable {

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
     * 保险种类
     */
    @NotBlank(message = "保险种类不能为空")
    private String insuranceType;

    /**
     * 保险公司
     */
    @NotBlank(message = "保险公司不能为空")
    private String company;

    /**
     * 保险金额
     */
    private BigDecimal amount;

    /**
     * 参保人数
     */
    private Integer insuredCount;

    /**
     * 保险日期
     */
    @NotNull(message = "保险日期不能为空")
    private LocalDate startDate;

    /**
     * 到期日期
     */
    @NotNull(message = "到期日期不能为空")
    private LocalDate endDate;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 负责人电话
     */
    private String managerPhone;

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
        if (this.endDate != null) {
            LocalDate now = LocalDate.now();
            this.remainingDays = ChronoUnit.DAYS.between(now, this.endDate);
            if (this.remainingDays < 0) {
                this.checkStatus = "已过期";
            } else if (this.remainingDays <= 30) {
                this.checkStatus = "即将过期";
            } else {
                this.checkStatus = "正常";
            }
        } else {
            this.remainingDays = null;
            this.checkStatus = "未设置到期日期";
        }
    }
} 