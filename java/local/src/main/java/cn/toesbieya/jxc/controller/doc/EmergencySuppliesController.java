package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.EmergencySupplies;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.EmergencySuppliesSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.doc.EmergencySuppliesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 应急物资管理Controller
 */
@RestController
@RequestMapping("admin/document/emergency-supplies")
public class EmergencySuppliesController {

    @Autowired
    private EmergencySuppliesService emergencySuppliesService;

    /**
     * 分页查询应急物资
     */
    @PostMapping("search")
    public R search(@RequestBody EmergencySuppliesSearch search) {
        return emergencySuppliesService.page(search);
    }

    /**
     * 新增应急物资
     */
    @PostMapping
    public R add(@Valid @RequestBody EmergencySupplies supplies) {
        return emergencySuppliesService.add(supplies);
    }

    /**
     * 修改应急物资
     */
    @PutMapping
    public R update(@Valid @RequestBody EmergencySupplies supplies) {
        return emergencySuppliesService.update(supplies);
    }

    /**
     * 删除应急物资
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return emergencySuppliesService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 导出应急物资
     */
    @PostMapping("export")
    public void export(@RequestBody EmergencySuppliesSearch search, HttpServletResponse response) {
        emergencySuppliesService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return emergencySuppliesService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return emergencySuppliesService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return emergencySuppliesService.updateAlertStatus(updateVo);
    }
} 