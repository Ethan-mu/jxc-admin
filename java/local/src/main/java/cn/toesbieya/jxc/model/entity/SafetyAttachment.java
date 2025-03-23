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
 * 安全附件管理实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("biz_safety_attachment")
public class SafetyAttachment implements Serializable {

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
     * 单位
     */
    @NotBlank(message = "单位不能为空")
    private String unit;

    /**
     * 附件类型
     */
    @NotBlank(message = "附件类型不能为空")
    private String attachType;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    /**
     * 使用状态：正常，损坏，待更换
     */
    private String status;

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
     * 储存位置
     */
    private String storageLocation;

    /**
     * 使用日期
     */
    private LocalDate useDate;

    /**
     * 复检日期
     */
    private LocalDate recheckDate;

    /**
     * 预警状态：未设置, 待预警, 已提醒, 已关闭
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