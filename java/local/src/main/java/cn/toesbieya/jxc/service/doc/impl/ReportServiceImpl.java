package cn.toesbieya.jxc.service.doc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.toesbieya.jxc.model.entity.Report;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.ReportSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.ReportExport;
import cn.toesbieya.jxc.mapper.ReportMapper;
import cn.toesbieya.jxc.service.doc.ReportService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import cn.toesbieya.jxc.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报告管理Service实现类
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public R page(ReportSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new ReportSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Report> wrapper = buildQueryWrapper(search);

        // 执行分页查询
        Page<Report> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, wrapper);

        // 计算剩余天数
        List<Report> records = page.getRecords();
        records.forEach(this::calculateRemainingDays);

        return R.success(page);
    }

    @Override
    @Transactional
    public R add(Report report) {
        if (report == null) {
            return R.fail("参数错误");
        }
        
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        // 设置用户ID
        if (report.getUid() == null) {
            report.setUid(currentUser.getId());
            logger.info("为报告设置用户ID: {}", currentUser.getId());
        }
        
        // 设置默认值
        if (report.getAlertStatus() == null) {
            report.setAlertStatus("未设置");
        }
        if (report.getAlertCount() == null) {
            report.setAlertCount(0);
        }
        
        // 计算剩余天数
        calculateRemainingDays(report);
        
        // 保存报告
        boolean success = save(report);
        
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(Report report) {
        if (this.updateById(report)) {
            return R.success("更新成功");
        }
        return R.fail("更新失败");
    }

    @Override
    public void export(ReportSearch search, HttpServletResponse response) {
        try {
            // 参数防空处理
            if (search == null) {
                search = new ReportSearch();
            }
            
            // 构建查询条件
            LambdaQueryWrapper<Report> wrapper = buildQueryWrapper(search);
            List<Report> list = this.list(wrapper);
            
            if (list.isEmpty()) {
                WebUtil.responseJson(response, R.fail("没有可导出的数据"));
                return;
            }
            
            // 计算剩余天数
            list.forEach(this::calculateRemainingDays);
            
            // 转换为导出专用实体
            List<ReportExport> exportList = list.stream().map(this::convertToExport).collect(Collectors.toList());
    
            // 导出Excel
            ExcelUtil.exportSimply(exportList, response, "报告管理");
        } catch (Exception e) {
            logger.error("导出报告失败", e);
            try {
                WebUtil.responseJson(response, R.fail("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                logger.error("响应导出错误信息失败", ex);
            }
        }
    }
    
    /**
     * 将实体转换为导出专用实体
     */
    private ReportExport convertToExport(Report report) {
        ReportExport export = new ReportExport();
        
        // 复制基本字段
        export.setId(report.getId());
        export.setCertificateName(report.getCertificateName());
        export.setReportType(report.getReportType());
        export.setCompilingUnit(report.getCompilingUnit());
        export.setCompileDate(report.getCompileDate() != null ? report.getCompileDate().toString() : null);
        export.setRecordDate(report.getRecordDate() != null ? report.getRecordDate().toString() : null);
        export.setNextRecordDate(report.getNextRecordDate() != null ? report.getNextRecordDate().toString() : null);
        export.setRemainingDays(report.getRemainingDays());
        export.setStorageLocation(report.getStorageLocation());
        export.setManagerName(report.getManagerName());
        export.setManagerContact(report.getManagerContact());
        export.setLeaderName(report.getLeaderName());
        export.setLeaderContact(report.getLeaderContact());
        export.setAlertStatus(report.getAlertStatus());
        export.setAlertCount(report.getAlertCount());
        
        // 格式化创建时间
        if (report.getCreateTime() != null) {
            export.setCreateTime(report.getCreateTime().toString().replace("T", " "));
        }
        
        return export;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R setAlert(Integer id) {
        Report report = this.getById(id);
        if (report == null) {
            return R.fail("报告不存在");
        }
        
        report.setAlertStatus("待预警");
        if (this.updateById(report)) {
            return R.success("设置预警成功");
        }
        return R.fail("设置预警失败");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R closeAlert(Integer id) {
        Report report = this.getById(id);
        if (report == null) {
            return R.fail("报告不存在");
        }
        
        report.setAlertStatus("已关闭");
        if (this.updateById(report)) {
            return R.success("关闭预警成功");
        }
        return R.fail("关闭预警失败");
    }
    
    @Override
    @Transactional
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        if (updateVo == null || updateVo.getId() == null) {
            return R.fail("参数错误");
        }
        
        Report report = this.getById(updateVo.getId());
        if (report == null) {
            return R.fail("报告不存在");
        }
        
        String alertStatus = updateVo.getStatus();
        
        report.setAlertStatus(alertStatus);
        
        // 如果是已提醒状态，增加预警次数
        if ("已提醒".equals(alertStatus)) {
            Integer alertCount = report.getAlertCount();
            report.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }
        
        boolean success = this.updateById(report);
        return success ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }
    
    /**
     * 构建查询条件
     *
     * @param search 查询参数
     * @return 查询条件
     */
    private LambdaQueryWrapper<Report> buildQueryWrapper(ReportSearch search) {
        LambdaQueryWrapper<Report> wrapper = new LambdaQueryWrapper<>();
        
        // 搜索参数为空则直接返回基本查询
        if (search == null) {
            return wrapper.orderByDesc(Report::getCreateTime);
        }
        
        // 获取当前登录用户，添加用户ID过滤条件
        UserVo currentUser = SessionUtil.get();
        if (currentUser != null) {
            wrapper.eq(Report::getUid, currentUser.getId());
            logger.info("查询限制用户ID: {}", currentUser.getId());
        }
        
        // 添加其他查询条件
        if (!StringUtils.isEmpty(search.getCertificateName())) {
            wrapper.like(Report::getCertificateName, search.getCertificateName());
        }
        
        if (!StringUtils.isEmpty(search.getReportType())) {
            wrapper.like(Report::getReportType, search.getReportType());
        }
        
        if (!StringUtils.isEmpty(search.getCompilingUnit())) {
            wrapper.like(Report::getCompilingUnit, search.getCompilingUnit());
        }
        
        if (!StringUtils.isEmpty(search.getManagerName())) {
            wrapper.like(Report::getManagerName, search.getManagerName());
        }
        
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            wrapper.eq(Report::getAlertStatus, search.getAlertStatus());
        }
        
        // 编制日期范围
        if (search.getCompileDateStart() != null) {
            wrapper.ge(Report::getCompileDate, search.getCompileDateStart());
        }
        
        if (search.getCompileDateEnd() != null) {
            wrapper.le(Report::getCompileDate, search.getCompileDateEnd());
        }
        
        // 备案日期范围
        if (search.getRecordDateStart() != null) {
            wrapper.ge(Report::getRecordDate, search.getRecordDateStart());
        }
        
        if (search.getRecordDateEnd() != null) {
            wrapper.le(Report::getRecordDate, search.getRecordDateEnd());
        }
        
        // 默认按创建时间降序排序
        wrapper.orderByDesc(Report::getCreateTime);
        
        return wrapper;
    }
    
    /**
     * 计算剩余天数
     * 
     * @param report 报告实体
     */
    private void calculateRemainingDays(Report report) {
        if (report.getNextRecordDate() != null) {
            // 计算下次备案/备查时间与当前日期的差值
            LocalDate now = LocalDate.now();
            long days = ChronoUnit.DAYS.between(now, report.getNextRecordDate());
            report.setRemainingDays(days);
        } else {
            // 如果没有设置下次备案/备查时间，则剩余天数为null
            report.setRemainingDays(null);
        }
    }
} 