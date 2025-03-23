package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.SafetyInspection;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SafetyInspectionSearch;
import cn.toesbieya.jxc.service.doc.SafetyInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安全隐患排查Controller
 */
@RestController
@RequestMapping("admin/document/safety-inspection")
public class SafetyInspectionController {

    @Autowired
    private SafetyInspectionService safetyInspectionService;

    /**
     * 分页查询安全隐患排查
     */
    @PostMapping("search")
    public R search(@RequestBody SafetyInspectionSearch search) {
        return safetyInspectionService.page(search);
    }

    /**
     * 新增安全隐患排查
     */
    @PostMapping
    public R add(@Valid @RequestBody SafetyInspection inspection) {
        return safetyInspectionService.add(inspection);
    }

    /**
     * 修改安全隐患排查
     */
    @PutMapping
    public R update(@Valid @RequestBody SafetyInspection inspection) {
        return safetyInspectionService.update(inspection);
    }

    /**
     * 删除安全隐患排查
     */
    @DeleteMapping("delete")
    public R delete(@RequestParam Integer id) {
        return safetyInspectionService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }

    /**
     * 批量删除安全隐患排查
     */
    @DeleteMapping("batchDelete")
    public R batchDelete(@RequestBody List<Integer> ids) {
        return safetyInspectionService.removeByIds(ids) ? R.success("批量删除成功") : R.fail("批量删除失败");
    }

    /**
     * 导出安全隐患排查
     */
    @PostMapping("export")
    public void export(@RequestBody SafetyInspectionSearch search, HttpServletResponse response) {
        safetyInspectionService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set")
    public R setAlert(@RequestParam Integer id) {
        return safetyInspectionService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close")
    public R closeAlert(@RequestParam Integer id) {
        return safetyInspectionService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@RequestBody AlertStatusUpdateVo updateVo) {
        return safetyInspectionService.updateAlertStatus(updateVo);
    }
    
    /**
     * 获取安全隐患统计信息
     */
    @GetMapping("statistics")
    public R getStatistics() {
        // TODO: 实现统计逻辑
        List<Map<String, Object>> levelStats = new ArrayList<>();
        Map<String, Object> general = new HashMap<>();
        general.put("hazardLevel", "一般隐患");
        general.put("count", 10);
        levelStats.add(general);
        
        Map<String, Object> severe = new HashMap<>();
        severe.put("hazardLevel", "重大隐患");
        severe.put("count", 5);
        levelStats.add(severe);
        
        List<Map<String, Object>> resolveStats = new ArrayList<>();
        Map<String, Object> resolved = new HashMap<>();
        resolved.put("status", "已整改");
        resolved.put("count", 8);
        resolveStats.add(resolved);
        
        Map<String, Object> unresolved = new HashMap<>();
        unresolved.put("status", "未整改");
        unresolved.put("count", 7);
        resolveStats.add(unresolved);
        
        Map<String, Object> result = new HashMap<>();
        result.put("levelStats", levelStats);
        result.put("resolveStats", resolveStats);
        result.put("urgentCount", 3);
        
        return R.success(result);
    }
} 