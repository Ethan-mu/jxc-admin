package cn.toesbieya.jxc.controller.training;

import cn.toesbieya.jxc.model.entity.UnplannedEducation;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.UnplannedEducationSearch;
import cn.toesbieya.jxc.service.training.UnplannedEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 计划外培训Controller
 */
@RestController
@RequestMapping("admin/training/unplanned-training")
public class UnplannedEducationController {
    
    @Autowired
    private UnplannedEducationService educationService;
    
    /**
     * 分页查询计划外培训记录
     */
    @PostMapping("search")
    public R search(@RequestBody UnplannedEducationSearch search) {
        return educationService.page(search);
    }
    
    /**
     * 新增计划外培训记录
     */
    @PostMapping
    public R add(@Valid @RequestBody UnplannedEducation education) {
        return educationService.add(education);
    }
    
    /**
     * 修改计划外培训记录
     */
    @PutMapping
    public R update(@Valid @RequestBody UnplannedEducation education) {
        return educationService.update(education);
    }
    
    /**
     * 删除计划外培训记录
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return educationService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }
    
    /**
     * 导出计划外培训记录
     */
    @PostMapping("export")
    public void export(@RequestBody UnplannedEducationSearch search, HttpServletResponse response) {
        educationService.export(search, response);
    }
} 