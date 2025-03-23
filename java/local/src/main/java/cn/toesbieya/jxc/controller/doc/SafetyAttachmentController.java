package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.SafetyAttachment;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.SafetyAttachmentSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.doc.SafetyAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 安全附件管理Controller
 */
@RestController
@RequestMapping("admin/document/safety-attachment")
public class SafetyAttachmentController {

    @Autowired
    private SafetyAttachmentService safetyAttachmentService;

    /**
     * 分页查询安全附件
     */
    @PostMapping("search")
    public R search(@RequestBody SafetyAttachmentSearch search) {
        return safetyAttachmentService.page(search);
    }

    /**
     * 新增安全附件
     */
    @PostMapping
    public R add(@Valid @RequestBody SafetyAttachment attachment) {
        return safetyAttachmentService.add(attachment);
    }
    
    /**
     * 上传并保存附件
     */
    @PostMapping("upload")
    public R upload(@RequestParam("file") MultipartFile file, SafetyAttachment attachment) {
        return safetyAttachmentService.uploadAndSave(file, attachment);
    }

    /**
     * 修改安全附件
     */
    @PutMapping
    public R update(@Valid @RequestBody SafetyAttachment attachment) {
        return safetyAttachmentService.update(attachment);
    }

    /**
     * 删除安全附件
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return safetyAttachmentService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 导出安全附件
     */
    @PostMapping("export")
    public void export(@RequestBody SafetyAttachmentSearch search, HttpServletResponse response) {
        safetyAttachmentService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return safetyAttachmentService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return safetyAttachmentService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return safetyAttachmentService.updateAlertStatus(updateVo);
    }
    
    /**
     * 审核
     */
    @PutMapping("approve/{id}")
    public R approve(
            @PathVariable Integer id, 
            @RequestParam String status, 
            @RequestParam(required = false) String opinion) {
        return safetyAttachmentService.approve(id, status, opinion);
    }
} 