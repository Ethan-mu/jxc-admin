package cn.toesbieya.jxc.service.training.impl;

import cn.toesbieya.jxc.mapper.UnplannedEducationMapper;
import cn.toesbieya.jxc.model.entity.UnplannedEducation;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.UnplannedEducationSearch;
import cn.toesbieya.jxc.service.training.UnplannedEducationService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划外培训Service实现类
 */
@Service
public class UnplannedEducationServiceImpl extends ServiceImpl<UnplannedEducationMapper, UnplannedEducation> implements UnplannedEducationService {

    @Override
    public R page(UnplannedEducationSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new UnplannedEducationSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        LambdaQueryWrapper<UnplannedEducation> queryWrapper = getQueryWrapper(search);
        
        Page<UnplannedEducation> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        
        return R.success(result);
    }

    @Override
    public R add(UnplannedEducation education) {
        // 设置用户ID
        education.setUserId(ThreadUtil.getUser().getId());
        
        boolean success = this.save(education);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(UnplannedEducation education) {
        boolean success = this.updateById(education);
        return success ? R.success("修改成功") : R.fail("修改失败");
    }

    @Override
    public void export(UnplannedEducationSearch search, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<UnplannedEducation> queryWrapper = getQueryWrapper(search);
            List<UnplannedEducation> list = this.list(queryWrapper);
            
            List<Map<String, Object>> exportList = new ArrayList<>();
            
            for (UnplannedEducation education : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("员工姓名", education.getEmployeeName());
                map.put("性别", education.getGender());
                map.put("年龄", education.getAge());
                map.put("联系电话", education.getPhone());
                map.put("原岗位", education.getOriginalJob());
                map.put("现岗位", education.getCurrentJob());
                map.put("入职日期", education.getJoinDate());
                map.put("身份证号", education.getIdNumber());
                map.put("培训日期", education.getTrainingDate());
                map.put("培训地点", education.getTrainingLocation());
                map.put("培训讲师", education.getLecturer());
                map.put("课程内容", education.getCourseContent());
                map.put("考核成绩", education.getAssessmentResult());
                
                exportList.add(map);
            }
            
            ExcelUtil.exportSimply(exportList, response, "计划外培训记录");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取查询条件
     */
    private LambdaQueryWrapper<UnplannedEducation> getQueryWrapper(UnplannedEducationSearch search) {
        LambdaQueryWrapper<UnplannedEducation> wrapper = Wrappers.lambdaQuery();
        
        if (search != null) {
            // 员工姓名查询
            if (!StringUtils.isEmpty(search.getEmployeeName())) {
                wrapper.like(UnplannedEducation::getEmployeeName, search.getEmployeeName());
            }
            
            // 身份证号查询
            if (!StringUtils.isEmpty(search.getIdNumber())) {
                wrapper.like(UnplannedEducation::getIdNumber, search.getIdNumber());
            }
            
            // 当前岗位查询
            if (!StringUtils.isEmpty(search.getCurrentJob())) {
                wrapper.like(UnplannedEducation::getCurrentJob, search.getCurrentJob());
            }
            
            // 培训日期范围查询
            if (!StringUtils.isEmpty(search.getStartDate())) {
                LocalDate startDate = LocalDate.parse(search.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                wrapper.ge(UnplannedEducation::getTrainingDate, startDate);
            }
            
            if (!StringUtils.isEmpty(search.getEndDate())) {
                LocalDate endDate = LocalDate.parse(search.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                wrapper.le(UnplannedEducation::getTrainingDate, endDate);
            }
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(UnplannedEducation::getCreatedTime);
        
        return wrapper;
    }
} 