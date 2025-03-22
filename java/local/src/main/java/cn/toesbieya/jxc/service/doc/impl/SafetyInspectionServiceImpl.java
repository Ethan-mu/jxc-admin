package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.SafetyInspectionMapper;
import cn.toesbieya.jxc.model.entity.SafetyInspection;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.SafetyInspectionExport;
import cn.toesbieya.jxc.model.vo.search.SafetyInspectionSearch;
import cn.toesbieya.jxc.service.doc.SafetyInspectionService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import cn.toesbieya.jxc.util.WebUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 安全隐患排查Service实现类
 */
@Service
@Slf4j
public class SafetyInspectionServiceImpl extends ServiceImpl<SafetyInspectionMapper, SafetyInspection> implements SafetyInspectionService {

    @Override
    public R page(SafetyInspectionSearch search) {
        Page<SafetyInspection> page = new Page<>(search.getPage(), search.getPageSize());
        LambdaQueryWrapper<SafetyInspection> wrapper = getQueryWrapper(search);
        
        baseMapper.selectPage(page, wrapper);
        
        // 计算剩余天数（整改期限与当前日期的差值）
        LocalDate now = LocalDate.now();
        List<SafetyInspection> records = page.getRecords();
        
        records.forEach(item -> {
            // 整改是否已完成
            boolean isResolved = item.getIsResolved() != null && item.getIsResolved() == 1;
            
            // 如果已整改或没有设置整改期限，则不计算剩余天数
            if (isResolved || item.getDeadline() == null) {
                return;
            }
            
            // 计算剩余天数
            long remainingDays = ChronoUnit.DAYS.between(now, item.getDeadline());
            
            // 根据剩余天数设置检查状态
            if (remainingDays < 0) {
                item.setCheckStatus("已过期");
            } else if (remainingDays <= 7) {
                item.setCheckStatus("即将过期");
            } else {
                item.setCheckStatus("正常");
            }
        });
        
        // 创建Map存储分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", page.getRecords());
        result.put("total", page.getTotal());
        
        return R.success(result);
    }

    @Override
    public R add(SafetyInspection inspection) {
        // 设置用户ID
        UserVo user = SessionUtil.get();
        if (user != null) {
            inspection.setUid(user.getId());
        }
        
        // 设置初始预警状态和次数
        inspection.setAlertStatus("未设置");
        inspection.setAlertCount(0);
        
        // 设置默认值
        if (inspection.getIsResolved() == null) {
            inspection.setIsResolved(0);
        }
        
        boolean success = save(inspection);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(SafetyInspection inspection) {
        // 获取原记录
        SafetyInspection original = getById(inspection.getId());
        if (original == null) {
            return R.fail("记录不存在");
        }
        
        // 不允许修改用户ID
        inspection.setUid(original.getUid());
        
        // 保持原始预警状态和次数
        inspection.setAlertStatus(original.getAlertStatus());
        inspection.setAlertCount(original.getAlertCount());
        
        boolean success = updateById(inspection);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    public void export(SafetyInspectionSearch search, HttpServletResponse response) {
        try {
            // 参数防空处理
            if (search == null) {
                search = new SafetyInspectionSearch();
            }
            
            // 构建查询条件
            LambdaQueryWrapper<SafetyInspection> wrapper = getQueryWrapper(search);
            List<SafetyInspection> list = this.list(wrapper);
            
            if (list.isEmpty()) {
                WebUtil.responseJson(response, R.fail("没有可导出的数据"));
                return;
            }
            
            // 计算剩余天数和状态
            LocalDate now = LocalDate.now();
            list.forEach(item -> {
                // 整改是否已完成
                boolean isResolved = item.getIsResolved() != null && item.getIsResolved() == 1;
                
                // 如果已整改或没有设置整改期限，则不计算剩余天数
                if (isResolved || item.getDeadline() == null) {
                    return;
                }
                
                // 计算剩余天数
                long remainingDays = ChronoUnit.DAYS.between(now, item.getDeadline());
                
                // 根据剩余天数设置检查状态
                if (remainingDays < 0) {
                    item.setCheckStatus("已过期");
                } else if (remainingDays <= 7) {
                    item.setCheckStatus("即将过期");
                } else {
                    item.setCheckStatus("正常");
                }
            });
            
            // 转换为导出专用实体
            List<SafetyInspectionExport> exportList = list.stream()
                    .map(SafetyInspectionExport::new)
                    .collect(Collectors.toList());
            
            // 执行导出
            ExcelUtil.exportSimply(exportList, response, "安全隐患排查列表");
        } catch (Exception e) {
            log.error("导出安全隐患排查列表失败", e);
            try {
                WebUtil.responseJson(response, R.fail("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                log.error("响应导出错误信息失败", ex);
            }
        }
    }

    @Override
    public R setAlert(Integer id) {
        SafetyInspection inspection = getById(id);
        if (inspection == null) {
            return R.fail("记录不存在");
        }
        
        inspection.setAlertStatus("待预警");
        boolean success = updateById(inspection);
        return success ? R.success("设置预警成功") : R.fail("设置预警失败");
    }

    @Override
    public R closeAlert(Integer id) {
        SafetyInspection inspection = getById(id);
        if (inspection == null) {
            return R.fail("记录不存在");
        }
        
        inspection.setAlertStatus("已关闭");
        boolean success = updateById(inspection);
        return success ? R.success("关闭预警成功") : R.fail("关闭预警失败");
    }

    @Override
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        SafetyInspection inspection = getById(updateVo.getId());
        if (inspection == null) {
            return R.fail("记录不存在");
        }
        
        // 设置预警状态
        inspection.setAlertStatus(updateVo.getStatus());
        
        // 如果状态为"已提醒"，则预警次数加1
        if ("已提醒".equals(updateVo.getStatus())) {
            Integer alertCount = inspection.getAlertCount();
            inspection.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }
        
        boolean success = updateById(inspection);
        return success ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }

    @Override
    public R delete(Integer id) {
        return removeById(id) ? R.success("删除成功") : R.fail("删除失败");
    }
    
    @Override
    public R batchDelete(List<Integer> ids) {
        return removeByIds(ids) ? R.success("批量删除成功") : R.fail("批量删除失败");
    }
    
    @Override
    public R getStatistics() {
        LocalDate now = LocalDate.now();
        
        // 查询所有未解决的安全隐患
        LambdaQueryWrapper<SafetyInspection> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SafetyInspection::getIsResolved, 0);
        List<SafetyInspection> inspections = this.list(wrapper);
        
        // 统计危害等级分布
        Map<String, Long> levelCount = inspections.stream()
                .collect(Collectors.groupingBy(SafetyInspection::getHazardLevel, Collectors.counting()));
        
        // 计算即将到期的隐患数量（7天内）
        long urgentCount = inspections.stream()
                .filter(i -> i.getDeadline() != null)
                .filter(i -> ChronoUnit.DAYS.between(now, i.getDeadline()) <= 7 && ChronoUnit.DAYS.between(now, i.getDeadline()) >= 0)
                .count();
        
        // 统计整改状态分布
        long unresolvedCount = inspections.size();
        
        // 查询已解决的安全隐患数量
        LambdaQueryWrapper<SafetyInspection> resolvedWrapper = new LambdaQueryWrapper<>();
        resolvedWrapper.eq(SafetyInspection::getIsResolved, 1);
        long resolvedCount = this.count(resolvedWrapper);
        
        // 构建返回结果
        List<Map<String, Object>> levelStats = new ArrayList<>();
        levelCount.forEach((level, count) -> {
            Map<String, Object> stat = new HashMap<>();
            stat.put("hazardLevel", level);
            stat.put("count", count);
            levelStats.add(stat);
        });
        
        List<Map<String, Object>> resolveStats = new ArrayList<>();
        Map<String, Object> resolvedStat = new HashMap<>();
        resolvedStat.put("status", "已整改");
        resolvedStat.put("count", resolvedCount);
        resolveStats.add(resolvedStat);
        
        Map<String, Object> unresolvedStat = new HashMap<>();
        unresolvedStat.put("status", "未整改");
        unresolvedStat.put("count", unresolvedCount);
        resolveStats.add(unresolvedStat);
        
        Map<String, Object> result = new HashMap<>();
        result.put("levelStats", levelStats);
        result.put("resolveStats", resolveStats);
        result.put("urgentCount", urgentCount);
        
        return R.success(result);
    }

    /**
     * 构建查询条件
     *
     * @param search 搜索条件
     * @return 查询条件构造器
     */
    private LambdaQueryWrapper<SafetyInspection> getQueryWrapper(SafetyInspectionSearch search) {
        LambdaQueryWrapper<SafetyInspection> wrapper = new LambdaQueryWrapper<>();
        
        // 根据检查人员模糊查询
        if (!StringUtils.isEmpty(search.getInspector())) {
            wrapper.like(SafetyInspection::getInspector, search.getInspector());
        }
        
        // 根据隐患等级精确查询
        if (!StringUtils.isEmpty(search.getHazardLevel())) {
            wrapper.eq(SafetyInspection::getHazardLevel, search.getHazardLevel());
        }
        
        // 根据整改负责人模糊查询
        if (!StringUtils.isEmpty(search.getResponsiblePerson())) {
            wrapper.like(SafetyInspection::getResponsiblePerson, search.getResponsiblePerson());
        }
        
        // 根据公示情况精确查询
        if (!StringUtils.isEmpty(search.getPublicityStatus())) {
            wrapper.eq(SafetyInspection::getPublicityStatus, search.getPublicityStatus());
        }
        
        // 根据上报情况精确查询
        if (!StringUtils.isEmpty(search.getReportStatus())) {
            wrapper.eq(SafetyInspection::getReportStatus, search.getReportStatus());
        }
        
        // 根据是否已整改精确查询
        if (search.getIsResolved() != null) {
            wrapper.eq(SafetyInspection::getIsResolved, search.getIsResolved());
        }
        
        // 根据预警状态精确查询
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            wrapper.eq(SafetyInspection::getAlertStatus, search.getAlertStatus());
        }
        
        // 整改期限日期范围查询
        if (search.getDeadlineBegin() != null) {
            wrapper.ge(SafetyInspection::getDeadline, search.getDeadlineBegin());
        }
        if (search.getDeadlineEnd() != null) {
            wrapper.le(SafetyInspection::getDeadline, search.getDeadlineEnd());
        }
        
        // 隐患整改时间范围查询
        if (search.getRectifyDateBegin() != null) {
            wrapper.ge(SafetyInspection::getRectifyDate, search.getRectifyDateBegin());
        }
        if (search.getRectifyDateEnd() != null) {
            wrapper.le(SafetyInspection::getRectifyDate, search.getRectifyDateEnd());
        }
        
        // 验证时间范围查询
        if (search.getVerifyDateBegin() != null) {
            wrapper.ge(SafetyInspection::getVerifyDate, search.getVerifyDateBegin());
        }
        if (search.getVerifyDateEnd() != null) {
            wrapper.le(SafetyInspection::getVerifyDate, search.getVerifyDateEnd());
        }
        
        // 默认按创建时间降序排序
        wrapper.orderByDesc(SafetyInspection::getCreateTime);
        
        return wrapper;
    }
} 