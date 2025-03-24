package cn.toesbieya.jxc.controller.training;

import cn.toesbieya.jxc.model.entity.ReturnJobTraining;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.ReturnJobTrainingSearch;
import cn.toesbieya.jxc.service.training.ReturnJobTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 复岗教育培训Controller
 */
@RestController
@RequestMapping("admin/training/return-job")
public class ReturnJobTrainingController {
    
    @Autowired
    private ReturnJobTrainingService trainingService;
    
    /**
     * 分页查询复岗教育培训记录
     */
    @PostMapping("search")
    public R search(@RequestBody ReturnJobTrainingSearch search) {
        return trainingService.page(search);
    }
    
    /**
     * 新增复岗教育培训记录
     */
    @PostMapping
    public R add(@Valid @RequestBody ReturnJobTraining training) {
        return trainingService.add(training);
    }
    
    /**
     * 修改复岗教育培训记录
     */
    @PutMapping
    public R update(@Valid @RequestBody ReturnJobTraining training) {
        return trainingService.update(training);
    }
    
    /**
     * 删除复岗教育培训记录
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return trainingService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }
    
    /**
     * 导出复岗教育培训记录
     */
    @PostMapping("export")
    public void export(@RequestBody ReturnJobTrainingSearch search, HttpServletResponse response) {
        trainingService.export(search, response);
    }
} 