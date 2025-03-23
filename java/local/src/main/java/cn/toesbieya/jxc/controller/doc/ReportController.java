package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.Report;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.ReportSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.doc.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 报告管理Controller
 */
@RestController
@RequestMapping("admin/document/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 分页查询报告
     */
    @PostMapping("search")
    public R search(@RequestBody ReportSearch search) {
        return reportService.page(search);
    }

    /**
     * 新增报告
     */
    @PostMapping
    public R add(@Valid @RequestBody Report report) {
        return reportService.add(report);
    }

    /**
     * 修改报告
     */
    @PutMapping
    public R update(@Valid @RequestBody Report report) {
        return reportService.update(report);
    }

    /**
     * 删除报告
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return reportService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 导出报告
     */
    @PostMapping("export")
    public void export(@RequestBody ReportSearch search, HttpServletResponse response) {
        reportService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return reportService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return reportService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return reportService.updateAlertStatus(updateVo);
    }
} 