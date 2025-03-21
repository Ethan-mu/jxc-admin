package cn.toesbieya.jxc.service.impl.doc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.toesbieya.jxc.model.entity.CompanyCertificate;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.CompanyCertificateSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.CompanyCertificateExport;
import cn.toesbieya.jxc.mapper.CompanyCertificateMapper;
import cn.toesbieya.jxc.service.doc.CompanyCertificateService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import cn.toesbieya.jxc.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公司资质证书Service实现类
 */
@Service
public class CompanyCertificateServiceImpl extends ServiceImpl<CompanyCertificateMapper, CompanyCertificate> implements CompanyCertificateService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyCertificateServiceImpl.class);

    @Override
    public R page(CompanyCertificateSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new CompanyCertificateSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        // 构建查询条件
        LambdaQueryWrapper<CompanyCertificate> wrapper = buildQueryWrapper(search);

        // 执行分页查询
        Page<CompanyCertificate> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, wrapper);

        // 转换结果
        List<CompanyCertificate> records = page.getRecords();
        records.forEach(this::calculateRemainingDays);

        return R.success(page);
    }

    @Override
    @Transactional
    public R add(CompanyCertificate certificate) {
        if (certificate == null) {
            return R.fail("参数错误");
        }
        
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        // 设置用户ID
        if (certificate.getUid() == null) {
            certificate.setUid(currentUser.getId());
            logger.info("为证书设置用户ID: {}", currentUser.getId());
        }
        
        // 设置默认值
        if (certificate.getAlertStatus() == null) {
            certificate.setAlertStatus("未设置");
        }
        if (certificate.getAlertCount() == null) {
            certificate.setAlertCount(30);
        }
        
        // 设置剩余天数
        calculateRemainingDays(certificate);
        
        // 保存证书
        boolean success = save(certificate);
        
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(CompanyCertificate certificate) {
        if (this.updateById(certificate)) {
            return R.success("更新成功");
        }
        return R.fail("更新失败");
    }

    @Override
    public void export(CompanyCertificateSearch search, HttpServletResponse response) {
        try {
            // 参数防空处理
            if (search == null) {
                search = new CompanyCertificateSearch();
            }
            
            // 构建查询条件
            LambdaQueryWrapper<CompanyCertificate> wrapper = buildQueryWrapper(search);
            List<CompanyCertificate> list = this.list(wrapper);
            
            if (list.isEmpty()) {
                WebUtil.responseJson(response, R.fail("没有可导出的数据"));
                return;
            }
            
            // 计算剩余天数
            list.forEach(this::calculateRemainingDays);
            
            // 转换为导出专用实体
            List<CompanyCertificateExport> exportList = list.stream().map(this::convertToExport).collect(Collectors.toList());
    
            // 导出Excel
            ExcelUtil.exportSimply(exportList, response, "公司资质证书");
        } catch (Exception e) {
            logger.error("导出公司资质证书失败", e);
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
    private CompanyCertificateExport convertToExport(CompanyCertificate certificate) {
        CompanyCertificateExport export = new CompanyCertificateExport();
        
        // 复制基本字段
        export.setId(certificate.getId());
        export.setCertificateName(certificate.getCertificateName());
        export.setIssuingAuthority(certificate.getIssuingAuthority());
        export.setIssueDate(certificate.getIssueDate() != null ? certificate.getIssueDate().toString() : null);
        export.setRenewalDate(certificate.getRenewalDate() != null ? certificate.getRenewalDate().toString() : null);
        export.setReviewDate(certificate.getReviewDate() != null ? certificate.getReviewDate().toString() : null);
        export.setRemainingDays(certificate.getRemainingDays());
        export.setStorageLocation(certificate.getStorageLocation());
        export.setManagerName(certificate.getManagerName());
        export.setManagerContact(certificate.getManagerContact());
        export.setLeaderName(certificate.getLeaderName());
        export.setLeaderContact(certificate.getLeaderContact());
        export.setAlertStatus(certificate.getAlertStatus());
        export.setAlertCount(certificate.getAlertCount());
        
        // 格式化创建时间
        if (certificate.getCreateTime() != null) {
            export.setCreateTime(certificate.getCreateTime().toString().replace("T", " "));
        }
        
        return export;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R setAlert(Integer id) {
        CompanyCertificate certificate = this.getById(id);
        if (certificate == null) {
            return R.fail("证书不存在");
        }
        
        certificate.setAlertStatus("待预警");
        if (this.updateById(certificate)) {
            return R.success("设置预警成功");
        }
        return R.fail("设置预警失败");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R closeAlert(Integer id) {
        CompanyCertificate certificate = this.getById(id);
        if (certificate == null) {
            return R.fail("证书不存在");
        }
        
        certificate.setAlertStatus("已关闭");
        if (this.updateById(certificate)) {
            return R.success("关闭预警成功");
        }
        return R.fail("关闭预警失败");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        CompanyCertificate certificate = this.getById(updateVo.getId());
        if (certificate == null) {
            return R.fail("证书不存在");
        }
        
        certificate.setAlertStatus(updateVo.getAlertStatus());
        
        // 如果是已提醒状态，增加预警次数
        if ("已提醒".equals(updateVo.getAlertStatus())) {
            Integer alertCount = certificate.getAlertCount();
            certificate.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }
        
        if (this.updateById(certificate)) {
            return R.success("更新预警状态成功");
        }
        return R.fail("更新预警状态失败");
    }
    
    /**
     * 构建查询条件
     *
     * @param search 查询参数
     * @return 查询条件
     */
    private LambdaQueryWrapper<CompanyCertificate> buildQueryWrapper(CompanyCertificateSearch search) {
        LambdaQueryWrapper<CompanyCertificate> wrapper = new LambdaQueryWrapper<>();
        
        // 搜索参数为空则直接返回基本查询
        if (search == null) {
            return wrapper.orderByDesc(CompanyCertificate::getCreateTime);
        }
        
        // 获取当前登录用户，添加用户ID过滤条件
        UserVo currentUser = SessionUtil.get();
        if (currentUser != null) {
            wrapper.eq(CompanyCertificate::getUid, currentUser.getId());
            logger.info("查询限制用户ID: {}", currentUser.getId());
        }
        
        // 添加查询条件
        wrapper.like(!StringUtils.isEmpty(search.getCertificateName()), 
                CompanyCertificate::getCertificateName, search.getCertificateName())
               .like(!StringUtils.isEmpty(search.getIssuingAuthority()), 
                   CompanyCertificate::getIssuingAuthority, search.getIssuingAuthority())
               .like(!StringUtils.isEmpty(search.getManagerName()), 
                   CompanyCertificate::getManagerName, search.getManagerName())
               .eq(!StringUtils.isEmpty(search.getAlertStatus()), 
                   CompanyCertificate::getAlertStatus, search.getAlertStatus());
        
        // 添加时间范围查询条件
        if (search.getIssueStartDate() != null) {
            wrapper.ge(CompanyCertificate::getIssueDate, search.getIssueStartDate());
        }
        if (search.getIssueEndDate() != null) {
            wrapper.le(CompanyCertificate::getIssueDate, search.getIssueEndDate());
        }
        if (search.getRenewalStartDate() != null) {
            wrapper.ge(CompanyCertificate::getRenewalDate, search.getRenewalStartDate());
        }
        if (search.getRenewalEndDate() != null) {
            wrapper.le(CompanyCertificate::getRenewalDate, search.getRenewalEndDate());
        }
        
        // 默认按创建时间降序排序
        wrapper.orderByDesc(CompanyCertificate::getCreateTime);
        
        return wrapper;
    }
    
    /**
     * 计算剩余天数并暂存到transient字段
     *
     * @param certificate 证书实体
     */
    private void calculateRemainingDays(CompanyCertificate certificate) {
        // 计算复证或换证剩余天数（优先使用复证时间）
        LocalDate targetDate = certificate.getReviewDate() != null ? 
                            certificate.getReviewDate() : certificate.getRenewalDate();
        
        if (targetDate != null) {
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, targetDate);
            // 暂存剩余天数
            certificate.setRemainingDays(days);
        }
    }
} 