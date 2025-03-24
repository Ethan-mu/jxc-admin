package cn.toesbieya.jxc.service.training.impl;

import cn.toesbieya.jxc.mapper.ContinuingEducationMapper;
import cn.toesbieya.jxc.model.entity.ContinuingEducation;
import cn.toesbieya.jxc.model.vo.search.ContinuingEducationSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.training.ContinuingEducationService;
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
 * 继续教育培训Service实现类
 */
@Service
public class ContinuingEducationServiceImpl extends ServiceImpl<ContinuingEducationMapper, ContinuingEducation> implements ContinuingEducationService {

    @Override
    public R page(ContinuingEducationSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new ContinuingEducationSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        LambdaQueryWrapper<ContinuingEducation> queryWrapper = getQueryWrapper(search);
        
        Page<ContinuingEducation> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        
        return R.success(result);
    }

    @Override
    public R add(ContinuingEducation education) {
        // 设置用户ID
        education.setUid(ThreadUtil.getUser().getId());
        
        boolean success = this.save(education);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(ContinuingEducation education) {
        boolean success = this.updateById(education);
        return success ? R.success("修改成功") : R.fail("修改失败");
    }

    @Override
    public void export(ContinuingEducationSearch search, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<ContinuingEducation> queryWrapper = getQueryWrapper(search);
            List<ContinuingEducation> list = this.list(queryWrapper);
            
            List<Map<String, Object>> exportList = new ArrayList<>();
            
            for (ContinuingEducation education : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("员工姓名", education.getEmployeeName());
                map.put("性别", education.getGender());
                map.put("年龄", education.getAge());
                map.put("联系电话", education.getPhone());
                map.put("原工种", education.getOriginalJob());
                map.put("现工种", education.getCurrentJob());
                map.put("入职日期", education.getJoinDate());
                map.put("身份证号码", education.getIdNumber());
                map.put("培训日期", education.getTrainingDate());
                map.put("培训地点", education.getTrainingLocation());
                map.put("授课人", education.getLecturer());
                map.put("主讲内容", education.getCourseContent());
                map.put("考核成绩", education.getAssessmentResult());
                
                exportList.add(map);
            }
            
            ExcelUtil.exportSimply(exportList, response, "继续教育培训");
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
    private LambdaQueryWrapper<ContinuingEducation> getQueryWrapper(ContinuingEducationSearch search) {
        LambdaQueryWrapper<ContinuingEducation> queryWrapper = Wrappers.lambdaQuery();
        
        // 员工姓名条件
        if (!StringUtils.isEmpty(search.getEmployeeName())) {
            queryWrapper.like(ContinuingEducation::getEmployeeName, search.getEmployeeName());
        }
        
        // 身份证号码条件
        if (!StringUtils.isEmpty(search.getIdNumber())) {
            queryWrapper.like(ContinuingEducation::getIdNumber, search.getIdNumber());
        }
        
        // 现工种条件
        if (!StringUtils.isEmpty(search.getCurrentJob())) {
            queryWrapper.like(ContinuingEducation::getCurrentJob, search.getCurrentJob());
        }
        
        // 培训日期范围
        if (search.getTrainingDateStart() != null && !StringUtils.isEmpty(search.getTrainingDateStart())) {
            queryWrapper.ge(ContinuingEducation::getTrainingDate, search.getTrainingDateStart());
        }
        if (search.getTrainingDateEnd() != null && !StringUtils.isEmpty(search.getTrainingDateEnd())) {
            queryWrapper.le(ContinuingEducation::getTrainingDate, search.getTrainingDateEnd());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(ContinuingEducation::getCreatedTime);
        
        return queryWrapper;
    }
} 