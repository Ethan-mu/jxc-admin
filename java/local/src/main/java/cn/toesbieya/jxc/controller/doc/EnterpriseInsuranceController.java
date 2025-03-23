package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.EnterpriseInsurance;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.EnterpriseInsuranceSearch;
import cn.toesbieya.jxc.service.doc.EnterpriseInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 企业保险Controller
 */
@RestController
@RequestMapping("admin/document/enterprise-insurance")
public class EnterpriseInsuranceController {

    @Autowired
    private EnterpriseInsuranceService enterpriseInsuranceService;

    /**
     * 分页查询企业保险
     */
    @PostMapping("search")
    public R search(@RequestBody EnterpriseInsuranceSearch search) {
        return enterpriseInsuranceService.page(search);
    }

    /**
     * 新增企业保险
     */
    @PostMapping
    public R add(@Valid @RequestBody EnterpriseInsurance insurance) {
        return enterpriseInsuranceService.add(insurance);
    }

    /**
     * 修改企业保险
     */
    @PutMapping
    public R update(@Valid @RequestBody EnterpriseInsurance insurance) {
        return enterpriseInsuranceService.update(insurance);
    }

    /**
     * 删除企业保险
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return enterpriseInsuranceService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 导出企业保险
     */
    @PostMapping("export")
    public void export(@RequestBody EnterpriseInsuranceSearch search, HttpServletResponse response) {
        enterpriseInsuranceService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return enterpriseInsuranceService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return enterpriseInsuranceService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return enterpriseInsuranceService.updateAlertStatus(updateVo);
    }
} 