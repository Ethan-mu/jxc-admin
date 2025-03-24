package cn.toesbieya.jxc.controller.training;

import cn.toesbieya.jxc.model.entity.FourNewTraining;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.FourNewTrainingSearch;
import cn.toesbieya.jxc.service.training.FourNewTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 四新技术培训Controller
 */
@RestController
@RequestMapping("admin/training/four-new")
public class FourNewTrainingController {
    
    @Autowired
    private FourNewTrainingService trainingService;
    
    /**
     * 分页查询四新技术培训记录
     */
    @PostMapping("search")
    public R search(@RequestBody FourNewTrainingSearch search) {
        return trainingService.page(search);
    }
    
    /**
     * 新增四新技术培训记录
     */
    @PostMapping
    public R add(@Valid @RequestBody FourNewTraining training) {
        return trainingService.add(training);
    }
    
    /**
     * 修改四新技术培训记录
     */
    @PutMapping
    public R update(@Valid @RequestBody FourNewTraining training) {
        return trainingService.update(training);
    }
    
    /**
     * 删除四新技术培训记录
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        return trainingService.removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }
    
    /**
     * 导出四新技术培训记录
     */
    @PostMapping("export")
    public void export(@RequestBody FourNewTrainingSearch search, HttpServletResponse response) {
        trainingService.export(search, response);
    }
} 