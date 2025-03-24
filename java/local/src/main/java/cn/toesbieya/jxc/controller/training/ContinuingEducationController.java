package cn.toesbieya.jxc.controller.training;

import cn.toesbieya.jxc.model.entity.ContinuingEducation;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.ContinuingEducationSearch;
import cn.toesbieya.jxc.service.training.ContinuingEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 继续教育培训Controller
 */
@RestController
@RequestMapping("admin/training/continuing-education")
public class ContinuingEducationController {
    
    @Autowired
    private ContinuingEducationService educationService;
    
    /**
     * 分页查询继续教育培训记录
     */
    @PostMapping("search")
    public R search(@RequestBody ContinuingEducationSearch search) {
        return educationService.page(search);
    }
    
    /**
     * 新增继续教育培训记录
     */
    @PostMapping
    public R add(@Valid @RequestBody ContinuingEducation education) {
        return educationService.add(education);
    }
    
    /**
     * 修改继续教育培训记录
     */
    @PutMapping
    public R update(@Valid @RequestBody ContinuingEducation education) {
        return educationService.update(education);
    }
    
    /**
     * 删除继续教育培训记录
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return educationService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }
    
    /**
     * 导出继续教育培训记录
     */
    @PostMapping("export")
    public void export(@RequestBody ContinuingEducationSearch search, HttpServletResponse response) {
        educationService.export(search, response);
    }
} 