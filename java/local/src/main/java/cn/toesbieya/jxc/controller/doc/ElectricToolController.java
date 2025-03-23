package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.ElectricTool;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.ElectricToolSearch;
import cn.toesbieya.jxc.service.doc.ElectricToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 电动防护工器具Controller
 */
@RestController
@RequestMapping("admin/document/electric-tool")
public class ElectricToolController {

    @Autowired
    private ElectricToolService electricToolService;

    /**
     * 分页查询电动防护工器具
     */
    @PostMapping("search")
    public R search(@RequestBody ElectricToolSearch search) {
        return electricToolService.page(search);
    }

    /**
     * 新增电动防护工器具
     */
    @PostMapping
    public R add(@Valid @RequestBody ElectricTool tool) {
        return electricToolService.add(tool);
    }

    /**
     * 修改电动防护工器具
     */
    @PutMapping
    public R update(@Valid @RequestBody ElectricTool tool) {
        return electricToolService.update(tool);
    }

    /**
     * 删除电动防护工器具
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return electricToolService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 导出电动防护工器具
     */
    @PostMapping("export")
    public void export(@RequestBody ElectricToolSearch search, HttpServletResponse response) {
        electricToolService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return electricToolService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return electricToolService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return electricToolService.updateAlertStatus(updateVo);
    }
} 