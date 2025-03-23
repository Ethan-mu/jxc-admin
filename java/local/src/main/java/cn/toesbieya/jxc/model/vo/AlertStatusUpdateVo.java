package cn.toesbieya.jxc.model.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 预警状态更新VO
 */
@Data
public class AlertStatusUpdateVo {
    
    /**
     * 对象ID
     */
    @NotNull(message = "ID不能为空")
    private Integer id;
    
    /**
     * 预警状态
     */
    @NotNull(message = "预警状态不能为空")
    private String status;
} 