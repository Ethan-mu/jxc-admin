package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 复岗教育培训实体类
 */
@Data
@TableName("biz_return_job_training")
public class ReturnJobTraining {
    
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
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 联系电话
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
     * 原岗位
     */
    private String originalPost;

    /**
     * 现岗位
     */
    private String currentPost;

    /**
     * 培训级别
     */
    private String trainingLevel;

    /**
     * 培训时间
     */
    private LocalDate trainingDate;

    /**
     * 培训地点
     */
    private String trainingLocation;

    /**
     * 授课人
     */
    private String lecturer;

    /**
     * 主讲内容
     */
    private String mainContent;

    /**
     * 考核成绩
     */
    private String score;

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