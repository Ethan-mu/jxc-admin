package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.CompanyCertificate;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.CompanyCertificateSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.doc.CompanyCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 公司资质证书Controller
 */
@RestController
@RequestMapping("admin/document/qualification")
public class CompanyCertificateController {

    @Autowired
    private CompanyCertificateService companyCertificateService;

    /**
     * 分页查询公司资质证书
     */
    @PostMapping("search")
    public R search(@RequestBody CompanyCertificateSearch search) {
        return companyCertificateService.page(search);
    }

    /**
     * 新增公司资质证书
     */
    @PostMapping
    public R add(@Valid @RequestBody CompanyCertificate certificate) {
        return companyCertificateService.add(certificate);
    }

    /**
     * 修改公司资质证书
     */
    @PutMapping
    public R update(@Valid @RequestBody CompanyCertificate certificate) {
        return companyCertificateService.update(certificate);
    }

    /**
     * 删除公司资质证书
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return companyCertificateService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 导出公司资质证书
     */
    @PostMapping("export")
    public void export(@RequestBody CompanyCertificateSearch search, HttpServletResponse response) {
        companyCertificateService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return companyCertificateService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return companyCertificateService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return companyCertificateService.updateAlertStatus(updateVo);
    }
} 