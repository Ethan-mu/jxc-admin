package cn.toesbieya.jxc.service.training.impl;

import cn.toesbieya.jxc.mapper.FourNewTrainingMapper;
import cn.toesbieya.jxc.model.entity.FourNewTraining;
import cn.toesbieya.jxc.model.vo.search.FourNewTrainingSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.training.FourNewTrainingService;
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
 * 四新技术培训Service实现类
 */
@Service
public class FourNewTrainingServiceImpl extends ServiceImpl<FourNewTrainingMapper, FourNewTraining> implements FourNewTrainingService {

    @Override
    public R page(FourNewTrainingSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new FourNewTrainingSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        LambdaQueryWrapper<FourNewTraining> queryWrapper = getQueryWrapper(search);
        
        Page<FourNewTraining> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, queryWrapper);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        
        return R.success(result);
    }

    @Override
    public R add(FourNewTraining training) {
        // 设置用户ID
        training.setUid(ThreadUtil.getUser().getId());
        
        boolean success = this.save(training);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(FourNewTraining training) {
        boolean success = this.updateById(training);
        return success ? R.success("修改成功") : R.fail("修改失败");
    }

    @Override
    public void export(FourNewTrainingSearch search, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<FourNewTraining> queryWrapper = getQueryWrapper(search);
            List<FourNewTraining> list = this.list(queryWrapper);
            
            List<Map<String, Object>> exportList = new ArrayList<>();
            
            for (FourNewTraining training : list) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("员工姓名", training.getEmployeeName());
                map.put("性别", training.getGender());
                map.put("年龄", training.getAge());
                map.put("手机号码", training.getPhone());
                map.put("原工种", training.getOriginalJob());
                map.put("现工种", training.getCurrentJob());
                map.put("入职日期", training.getJoinDate());
                map.put("身份证号码", training.getIdNumber());
                map.put("培训日期", training.getTrainingDate());
                map.put("培训地点", training.getTrainingLocation());
                map.put("新工艺内容", training.getNewTechnology());
                map.put("新设备操作要点", training.getNewEquipment());
                map.put("新技术规范", training.getNewTechnique());
                map.put("新材料特性", training.getNewMaterial());
                map.put("主讲人", training.getLecturer());
                map.put("主讲内容", training.getMainContent());
                map.put("考核成绩", training.getScore());
                
                exportList.add(map);
            }
            
            ExcelUtil.exportSimply(exportList, response, "四新技术培训");
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
    private LambdaQueryWrapper<FourNewTraining> getQueryWrapper(FourNewTrainingSearch search) {
        LambdaQueryWrapper<FourNewTraining> queryWrapper = Wrappers.lambdaQuery();
        
        // 员工姓名条件
        if (!StringUtils.isEmpty(search.getEmployeeName())) {
            queryWrapper.like(FourNewTraining::getEmployeeName, search.getEmployeeName());
        }
        
        // 身份证号码条件
        if (!StringUtils.isEmpty(search.getIdNumber())) {
            queryWrapper.like(FourNewTraining::getIdNumber, search.getIdNumber());
        }
        
        // 现工种条件
        if (!StringUtils.isEmpty(search.getCurrentJob())) {
            queryWrapper.like(FourNewTraining::getCurrentJob, search.getCurrentJob());
        }
        
        // 培训日期范围
        if (search.getTrainingDateStart() != null) {
            queryWrapper.ge(FourNewTraining::getTrainingDate, search.getTrainingDateStart());
        }
        if (search.getTrainingDateEnd() != null) {
            queryWrapper.le(FourNewTraining::getTrainingDate, search.getTrainingDateEnd());
        }
        
        // 按创建时间降序排序
        queryWrapper.orderByDesc(FourNewTraining::getCreatedTime);
        
        return queryWrapper;
    }
} 