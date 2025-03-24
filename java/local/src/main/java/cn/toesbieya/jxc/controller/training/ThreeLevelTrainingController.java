package cn.toesbieya.jxc.controller.training;

import cn.toesbieya.jxc.model.entity.ThreeLevelTraining;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.ThreeLevelTrainingSearch;
import cn.toesbieya.jxc.service.training.ThreeLevelTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 三级教育培训Controller
 */
@RestController
@RequestMapping("admin/document/three-level-training")
public class ThreeLevelTrainingController {
    
    @Autowired
    private ThreeLevelTrainingService trainingService;
    
    /**
     * 分页查询三级教育培训记录
     */
    @PostMapping("search")
    public R search(@RequestBody ThreeLevelTrainingSearch search) {
        return trainingService.page(search);
    }
    
    /**
     * 新增三级教育培训记录
     */
    @PostMapping
    public R add(@Valid @RequestBody ThreeLevelTraining training) {
        return trainingService.add(training);
    }
    
    /**
     * 修改三级教育培训记录
     */
    @PutMapping
    public R update(@Valid @RequestBody ThreeLevelTraining training) {
        return trainingService.update(training);
    }
    
    /**
     * 删除三级教育培训记录
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return trainingService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }
    
    /**
     * 导出三级教育培训记录
     */
    @PostMapping("export")
    public void export(@RequestBody ThreeLevelTrainingSearch search, HttpServletResponse response) {
        trainingService.export(search, response);
    }
} 