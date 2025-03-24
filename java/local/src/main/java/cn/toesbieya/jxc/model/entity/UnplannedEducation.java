package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 计划外培训档案实体类
 */
@Data
@TableName("biz_unplanned_education")
public class UnplannedEducation {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空")
    private String employeeName;
    
    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    private String gender;
    
    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    private Integer age;
    
    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;
    
    /**
     * 原工种
     */
    @NotBlank(message = "原工种不能为空")
    private String originalJob;
    
    /**
     * 现工种
     */
    @NotBlank(message = "现工种不能为空")
    private String currentJob;
    
    /**
     * 入职日期
     */
    @NotNull(message = "入职日期不能为空")
    private LocalDate joinDate;
    
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String idNumber;
    
    /**
     * 培训日期
     */
    @NotNull(message = "培训日期不能为空")
    private LocalDate trainingDate;
    
    /**
     * 培训地点
     */
    @NotBlank(message = "培训地点不能为空")
    private String trainingLocation;
    
    /**
     * 培训讲师
     */
    @NotBlank(message = "培训讲师不能为空")
    private String lecturer;
    
    /**
     * 课程内容
     */
    @NotBlank(message = "课程内容不能为空")
    private String courseContent;
    
    /**
     * 考核成绩
     */
    @NotBlank(message = "考核成绩不能为空")
    private String assessmentResult;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
} 