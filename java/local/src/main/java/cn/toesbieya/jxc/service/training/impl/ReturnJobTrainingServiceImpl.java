package cn.toesbieya.jxc.service.training.impl;

import cn.toesbieya.jxc.mapper.ReturnJobTrainingMapper;
import cn.toesbieya.jxc.model.entity.ReturnJobTraining;
import cn.toesbieya.jxc.model.vo.search.ReturnJobTrainingSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.training.ReturnJobTrainingService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 复岗教育培训Service实现类
 */
@Service
public class ReturnJobTrainingServiceImpl extends ServiceImpl<ReturnJobTrainingMapper, ReturnJobTraining> implements ReturnJobTrainingService {

    @Override
    public R page(ReturnJobTrainingSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new ReturnJobTrainingSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        LambdaQueryWrapper<ReturnJobTraining> queryWrapper = getQueryWrapper(search);
        
        Page<ReturnJobTraining> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        
        return R.success(result);
    }

    @Override
    public R add(ReturnJobTraining training) {
        // 设置用户ID
        training.setUid(ThreadUtil.getUser().getId());
        
        boolean success = this.save(training);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(ReturnJobTraining training) {
        boolean success = this.updateById(training);
        return success ? R.success("修改成功") : R.fail("修改失败");
    }

    @Override
    public void export(ReturnJobTrainingSearch search, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<ReturnJobTraining> queryWrapper = getQueryWrapper(search);
            List<ReturnJobTraining> list = this.list(queryWrapper);
            
            List<Map<String, Object>> exportList = new ArrayList<>();
            
            for (ReturnJobTraining training : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("姓名", training.getName());
                map.put("性别", training.getGender());
                map.put("年龄", training.getAge());
                map.put("联系电话", training.getPhone());
                map.put("原工种", training.getOriginalJob());
                map.put("现工种", training.getCurrentJob());
                map.put("入职日期", training.getJoinDate());
                map.put("身份证号码", training.getIdNumber());
                map.put("原岗位", training.getOriginalPost());
                map.put("现岗位", training.getCurrentPost());
                map.put("培训级别", training.getTrainingLevel());
                map.put("培训时间", training.getTrainingDate());
                map.put("培训地点", training.getTrainingLocation());
                map.put("授课人", training.getLecturer());
                map.put("主讲内容", training.getMainContent());
                map.put("考核成绩", training.getScore());
                
                exportList.add(map);
            }
            
            ExcelUtil.exportSimply(exportList, response, "复岗教育培训");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 构建查询条件
     *
     * @param search 查询参数
     * @return 查询条件
     */
    private LambdaQueryWrapper<ReturnJobTraining> getQueryWrapper(ReturnJobTrainingSearch search) {
        LambdaQueryWrapper<ReturnJobTraining> queryWrapper = Wrappers.lambdaQuery();
        
        // 姓名条件
        if (!StringUtils.isEmpty(search.getName())) {
            queryWrapper.like(ReturnJobTraining::getName, search.getName());
        }
        
        // 身份证号码条件
        if (!StringUtils.isEmpty(search.getIdNumber())) {
            queryWrapper.like(ReturnJobTraining::getIdNumber, search.getIdNumber());
        }
        
        // 现工种条件
        if (!StringUtils.isEmpty(search.getCurrentJob())) {
            queryWrapper.like(ReturnJobTraining::getCurrentJob, search.getCurrentJob());
        }
        
        // 培训级别条件
        if (!StringUtils.isEmpty(search.getTrainingLevel())) {
            queryWrapper.eq(ReturnJobTraining::getTrainingLevel, search.getTrainingLevel());
        }
        
        // 培训日期范围
        if (search.getTrainingDateStart() != null) {
            queryWrapper.ge(ReturnJobTraining::getTrainingDate, search.getTrainingDateStart());
        }
        if (search.getTrainingDateEnd() != null) {
            queryWrapper.le(ReturnJobTraining::getTrainingDate, search.getTrainingDateEnd());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(ReturnJobTraining::getCreatedTime);
        
        return queryWrapper;
    }
} 