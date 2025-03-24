package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 继续教育培训实体类
 */
@Data
@TableName("biz_continuing_education")
public class ContinuingEducation {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 原工种
     */
    private String originalJob;

    /**
     * 现工种
     */
    private String currentJob;

    /**
     * 入职日期
     */
    private LocalDate joinDate;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 培训日期
     */
    private LocalDate trainingDate;

    /**
     * 培训地点
     */
    private String trainingLocation;

    /**
     * 授课人姓名
     */
    private String lecturer;

    /**
     * 主讲内容详情
     */
    private String courseContent;

    /**
     * 考核成绩
     */
    private String assessmentResult;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
} 