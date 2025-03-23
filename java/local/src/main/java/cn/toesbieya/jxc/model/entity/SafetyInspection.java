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

/**
 * 安全隐患排查实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("biz_safety_inspection")
public class SafetyInspection implements Serializable {

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
     * 检查人员
     */
    @NotBlank(message = "检查人员不能为空")
    private String inspector;

    /**
     * 隐患描述
     */
    @NotBlank(message = "隐患描述不能为空")
    private String hazardDesc;

    /**
     * 隐患等级
     */
    private String hazardLevel;

    /**
     * 整改措施
     */
    private String correctiveAction;

    /**
     * 整改期限
     */
    private LocalDate deadline;

    /**
     * 检查负责人电话
     */
    private String inspectorContact;

    /**
     * 整改资金来源
     */
    private String fundSource;

    /**
     * 整改负责人
     */
    private String responsiblePerson;

    /**
     * 整改负责人电话
     */
    private String respPersonPhone;

    /**
     * 隐患整改时间
     */
    private LocalDate rectifyDate;

    /**
     * 验收负责人
     */
    private String verifier;

    /**
     * 验证时间
     */
    private LocalDate verifyDate;

    /**
     * 公示情况
     */
    private String publicityStatus;

    /**
     * 上报情况
     */
    private String reportStatus;

    /**
     * 是否已经整改（0: 未整改, 1: 已整改）
     */
    private Integer isResolved;

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
     * 检查状态，用于前端展示，非数据库字段
     */
    @TableField(exist = false)
    private String checkStatus;
} 