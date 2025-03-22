package cn.toesbieya.jxc.service.doc;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.toesbieya.jxc.model.entity.SafetyAttachment;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.SafetyAttachmentSearch;
import cn.toesbieya.jxc.model.vo.R;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 安全附件管理Service接口
 */
public interface SafetyAttachmentService extends IService<SafetyAttachment> {

    /**
     * 分页查询安全附件
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(SafetyAttachmentSearch search);

    /**
     * 添加安全附件
     *
     * @param attachment 安全附件信息
     * @return 操作结果
     */
    R add(SafetyAttachment attachment);
    
    /**
     * 上传并保存附件
     *
     * @param file 附件文件
     * @param attachment 附件信息
     * @return 操作结果
     */
    R uploadAndSave(MultipartFile file, SafetyAttachment attachment);

    /**
     * 修改安全附件
     *
     * @param attachment 安全附件信息
     * @return 操作结果
     */
    R update(SafetyAttachment attachment);

    /**
     * 导出安全附件列表
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(SafetyAttachmentSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 安全附件ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 安全附件ID
     * @return 操作结果
     */
    R closeAlert(Integer id);
    
    /**
     * 更新预警状态
     *
     * @param updateVo 更新参数
     * @return 操作结果
     */
    R updateAlertStatus(AlertStatusUpdateVo updateVo);
    
    /**
     * 审核
     *
     * @param id 附件ID
     * @param status 审核状态
     * @param opinion 审核意见
     * @return 操作结果
     */
    R approve(Integer id, String status, String opinion);
} 