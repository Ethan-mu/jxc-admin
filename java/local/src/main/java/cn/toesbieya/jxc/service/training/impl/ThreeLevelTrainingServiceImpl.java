package cn.toesbieya.jxc.service.training.impl;

import cn.toesbieya.jxc.mapper.ThreeLevelTrainingMapper;
import cn.toesbieya.jxc.model.entity.ThreeLevelTraining;
import cn.toesbieya.jxc.model.vo.search.ThreeLevelTrainingSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.training.ThreeLevelTrainingService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
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
 * 三级教育培训Service实现类
 */
@Service
public class ThreeLevelTrainingServiceImpl extends ServiceImpl<ThreeLevelTrainingMapper, ThreeLevelTraining> implements ThreeLevelTrainingService {

    @Override
    public R page(ThreeLevelTrainingSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new ThreeLevelTrainingSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        LambdaQueryWrapper<ThreeLevelTraining> queryWrapper = getQueryWrapper(search);
        
        Page<ThreeLevelTraining> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        
        return R.success(result);
    }

    @Override
    public R add(ThreeLevelTraining training) {
        // 设置用户ID
        training.setUid(ThreadUtil.getUser().getId());
        
        boolean success = this.save(training);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(ThreeLevelTraining training) {
        boolean success = this.updateById(training);
        return success ? R.success("修改成功") : R.fail("修改失败");
    }

    @Override
    public void export(ThreeLevelTrainingSearch search, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<ThreeLevelTraining> queryWrapper = getQueryWrapper(search);
            List<ThreeLevelTraining> list = this.list(queryWrapper);
            
            List<Map<String, Object>> exportList = new ArrayList<>();
            
            for (ThreeLevelTraining training : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("姓名", training.getName());
                map.put("性别", training.getGender());
                map.put("年龄", training.getAge());
                map.put("电话", training.getPhone());
                map.put("原工种", training.getOriginalJob());
                map.put("现工种", training.getCurrentJob());
                map.put("入职日期", training.getJoinDate());
                map.put("身份证号码", training.getIdNumber());
                map.put("公司级培训时间", training.getCompanyTrainDate());
                map.put("公司级培训地点", training.getCompanyTrainLocation());
                map.put("公司级培训主讲", training.getCompanyTrainLecturer());
                map.put("公司级培训内容", training.getCompanyTrainContent());
                map.put("公司级培训成绩", training.getCompanyTrainScore());
                map.put("车间级培训时间", training.getWorkshopTrainDate());
                map.put("车间级培训地点", training.getWorkshopTrainLocation());
                map.put("车间级培训主讲", training.getWorkshopTrainLecturer());
                map.put("车间级培训内容", training.getWorkshopTrainContent());
                map.put("车间级培训成绩", training.getWorkshopTrainScore());
                map.put("班组级培训时间", training.getTeamTrainDate());
                map.put("班组级培训地点", training.getTeamTrainLocation());
                map.put("班组级培训主讲", training.getTeamTrainLecturer());
                map.put("班组级培训内容", training.getTeamTrainContent());
                map.put("班组级培训成绩", training.getTeamTrainScore());
                
                exportList.add(map);
            }
            
            ExcelUtil.exportSimply(exportList, response, "三级教育培训");
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
    private LambdaQueryWrapper<ThreeLevelTraining> getQueryWrapper(ThreeLevelTrainingSearch search) {
        LambdaQueryWrapper<ThreeLevelTraining> queryWrapper = Wrappers.lambdaQuery();
        
        // 姓名条件
        if (!StringUtils.isEmpty(search.getName())) {
            queryWrapper.like(ThreeLevelTraining::getName, search.getName());
        }
        
        // 身份证号码条件
        if (!StringUtils.isEmpty(search.getIdNumber())) {
            queryWrapper.like(ThreeLevelTraining::getIdNumber, search.getIdNumber());
        }
        
        // 现工种条件
        if (!StringUtils.isEmpty(search.getCurrentJob())) {
            queryWrapper.like(ThreeLevelTraining::getCurrentJob, search.getCurrentJob());
        }
        
        // 公司级培训时间范围
        if (search.getCompanyTrainDateStart() != null) {
            queryWrapper.ge(ThreeLevelTraining::getCompanyTrainDate, search.getCompanyTrainDateStart());
        }
        if (search.getCompanyTrainDateEnd() != null) {
            queryWrapper.le(ThreeLevelTraining::getCompanyTrainDate, search.getCompanyTrainDateEnd());
        }
        
        // 车间级培训时间范围
        if (search.getWorkshopTrainDateStart() != null) {
            queryWrapper.ge(ThreeLevelTraining::getWorkshopTrainDate, search.getWorkshopTrainDateStart());
        }
        if (search.getWorkshopTrainDateEnd() != null) {
            queryWrapper.le(ThreeLevelTraining::getWorkshopTrainDate, search.getWorkshopTrainDateEnd());
        }
        
        // 班组级培训时间范围
        if (search.getTeamTrainDateStart() != null) {
            queryWrapper.ge(ThreeLevelTraining::getTeamTrainDate, search.getTeamTrainDateStart());
        }
        if (search.getTeamTrainDateEnd() != null) {
            queryWrapper.le(ThreeLevelTraining::getTeamTrainDate, search.getTeamTrainDateEnd());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(ThreeLevelTraining::getCreatedTime);
        
        return queryWrapper;
    }
} 