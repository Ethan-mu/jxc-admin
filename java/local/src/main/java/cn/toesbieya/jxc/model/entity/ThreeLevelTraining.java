package cn.toesbieya.jxc.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 三级教育培训实体类
 */
@Data
@TableName("biz_three_level_training")
public class ThreeLevelTraining {
    
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
     * 电话
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
    private LocalDateTime joinDate;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 公司级培训时间
     */
    private LocalDate companyTrainDate;

    /**
     * 公司级培训地点
     */
    private String companyTrainLocation;

    /**
     * 公司级培训主讲
     */
    private String companyTrainLecturer;

    /**
     * 公司级培训内容
     */
    private String companyTrainContent;

    /**
     * 公司级培训成绩
     */
    private String companyTrainScore;

    /**
     * 车间级培训时间
     */
    private LocalDate workshopTrainDate;

    /**
     * 车间级培训地点
     */
    private String workshopTrainLocation;

    /**
     * 车间级培训主讲
     */
    private String workshopTrainLecturer;

    /**
     * 车间级培训内容
     */
    private String workshopTrainContent;

    /**
     * 车间级培训成绩
     */
    private String workshopTrainScore;

    /**
     * 班组级培训时间
     */
    private LocalDate teamTrainDate;

    /**
     * 班组级培训地点
     */
    private String teamTrainLocation;

    /**
     * 班组级培训主讲
     */
    private String teamTrainLecturer;

    /**
     * 班组级培训内容
     */
    private String teamTrainContent;

    /**
     * 班组级培训成绩
     */
    private String teamTrainScore;

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