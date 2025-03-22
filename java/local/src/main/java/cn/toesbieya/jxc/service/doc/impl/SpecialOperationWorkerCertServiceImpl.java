package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.SpecialOperationWorkerCertMapper;
import cn.toesbieya.jxc.model.entity.SpecialOperationWorkerCert;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.search.SpecialOperationWorkerCertSearch;
import cn.toesbieya.jxc.service.doc.SpecialOperationWorkerCertService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;

/**
 * 特种作业人员证书Service实现类
 */
@Service
public class SpecialOperationWorkerCertServiceImpl extends ServiceImpl<SpecialOperationWorkerCertMapper, SpecialOperationWorkerCert> implements SpecialOperationWorkerCertService {

    @Override
    public R page(SpecialOperationWorkerCertSearch search) {
        LambdaQueryWrapper<SpecialOperationWorkerCert> wrapper = Wrappers.<SpecialOperationWorkerCert>lambdaQuery();
        
        // 条件查询
        if (StringUtils.isNotBlank(search.getCertificateName())) {
            wrapper.like(SpecialOperationWorkerCert::getCertificateName, search.getCertificateName());
        }
        
        if (StringUtils.isNotBlank(search.getWorkerName())) {
            wrapper.like(SpecialOperationWorkerCert::getWorkerName, search.getWorkerName());
        }
        
        if (StringUtils.isNotBlank(search.getIdCard())) {
            wrapper.like(SpecialOperationWorkerCert::getIdCard, search.getIdCard());
        }
        
        if (StringUtils.isNotBlank(search.getCertType())) {
            wrapper.eq(SpecialOperationWorkerCert::getCertType, search.getCertType());
        }
        
        if (StringUtils.isNotBlank(search.getIssuer())) {
            wrapper.like(SpecialOperationWorkerCert::getIssuer, search.getIssuer());
        }
        
        if (StringUtils.isNotBlank(search.getIssueDateStart())) {
            wrapper.ge(SpecialOperationWorkerCert::getIssueDate, search.getIssueDateStart());
        }
        
        if (StringUtils.isNotBlank(search.getIssueDateEnd())) {
            wrapper.le(SpecialOperationWorkerCert::getIssueDate, search.getIssueDateEnd());
        }
        
        if (StringUtils.isNotBlank(search.getReviewDateStart())) {
            wrapper.ge(SpecialOperationWorkerCert::getReviewDate, search.getReviewDateStart());
        }
        
        if (StringUtils.isNotBlank(search.getReviewDateEnd())) {
            wrapper.le(SpecialOperationWorkerCert::getReviewDate, search.getReviewDateEnd());
        }
        
        if (StringUtils.isNotBlank(search.getAlertStatus())) {
            wrapper.eq(SpecialOperationWorkerCert::getAlertStatus, search.getAlertStatus());
        }
        
        // 分页查询
        IPage<SpecialOperationWorkerCert> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, wrapper);
        
        // 计算剩余天数和证书状态
        calculateRemainingDaysAndStatus(page.getRecords());
        
        return R.success(page);
    }

    @Override
    public R add(SpecialOperationWorkerCert cert) {
        // 参数校验
        if (cert == null) {
            return R.fail("参数错误");
        }
        
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        // 设置用户ID
        cert.setUid(currentUser.getId());
        
        // 重置ID，确保是新增
        cert.setId(null);
        
        // 设置默认值
        cert.setAlertCount(0);
        cert.setAlertStatus("未设置");
        
        // 保存证书
        boolean result = this.save(cert);
        return result ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(SpecialOperationWorkerCert cert) {
        // 参数校验
        if (cert == null || cert.getId() == null) {
            return R.fail("参数错误");
        }
        
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        // 校验ID
        SpecialOperationWorkerCert existCert = this.getById(cert.getId());
        if (existCert == null) {
            return R.fail("证书不存在");
        }
        
        // 非管理员只能修改自己创建的证书
        if (!currentUser.isAdmin() && !existCert.getUid().equals(currentUser.getId())) {
            return R.fail("您没有权限修改此证书");
        }
        
        // 保持原预警次数和用户ID
        cert.setAlertCount(existCert.getAlertCount());
        cert.setUid(existCert.getUid());
        
        boolean result = this.updateById(cert);
        return result ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    public R delete(Integer id) {
        // 参数校验
        if (id == null) {
            return R.fail("参数错误");
        }
        
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        // 校验证书是否存在
        SpecialOperationWorkerCert cert = this.getById(id);
        if (cert == null) {
            return R.fail("证书不存在");
        }
        
        // 非管理员只能删除自己创建的证书
        if (!currentUser.isAdmin() && !cert.getUid().equals(currentUser.getId())) {
            return R.fail("您没有权限删除此证书");
        }
        
        boolean result = this.removeById(id);
        return result ? R.success("删除成功") : R.fail("删除失败");
    }

    @Override
    public void export(SpecialOperationWorkerCertSearch search, HttpServletResponse response) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<SpecialOperationWorkerCert> wrapper = Wrappers.<SpecialOperationWorkerCert>lambdaQuery();
            
            if (StringUtils.isNotBlank(search.getCertificateName())) {
                wrapper.like(SpecialOperationWorkerCert::getCertificateName, search.getCertificateName());
            }
            
            if (StringUtils.isNotBlank(search.getWorkerName())) {
                wrapper.like(SpecialOperationWorkerCert::getWorkerName, search.getWorkerName());
            }
            
            if (StringUtils.isNotBlank(search.getIdCard())) {
                wrapper.like(SpecialOperationWorkerCert::getIdCard, search.getIdCard());
            }
            
            if (StringUtils.isNotBlank(search.getCertType())) {
                wrapper.eq(SpecialOperationWorkerCert::getCertType, search.getCertType());
            }
            
            if (StringUtils.isNotBlank(search.getIssuer())) {
                wrapper.like(SpecialOperationWorkerCert::getIssuer, search.getIssuer());
            }
            
            if (StringUtils.isNotBlank(search.getIssueDateStart())) {
                wrapper.ge(SpecialOperationWorkerCert::getIssueDate, search.getIssueDateStart());
            }
            
            if (StringUtils.isNotBlank(search.getIssueDateEnd())) {
                wrapper.le(SpecialOperationWorkerCert::getIssueDate, search.getIssueDateEnd());
            }
            
            if (StringUtils.isNotBlank(search.getReviewDateStart())) {
                wrapper.ge(SpecialOperationWorkerCert::getReviewDate, search.getReviewDateStart());
            }
            
            if (StringUtils.isNotBlank(search.getReviewDateEnd())) {
                wrapper.le(SpecialOperationWorkerCert::getReviewDate, search.getReviewDateEnd());
            }
            
            if (StringUtils.isNotBlank(search.getAlertStatus())) {
                wrapper.eq(SpecialOperationWorkerCert::getAlertStatus, search.getAlertStatus());
            }
            
            // 获取数据并计算剩余天数和状态
            List<SpecialOperationWorkerCert> list = this.list(wrapper);
            calculateRemainingDaysAndStatus(list);
            
            // 定义Excel表头
            List<Map<String, String>> head = new ArrayList<>();
            this.addExcelHeader(head, "证书名称", "certificateName");
            this.addExcelHeader(head, "持证人姓名", "workerName");
            this.addExcelHeader(head, "联系方式", "workerContact");
            this.addExcelHeader(head, "身份证号", "idCard");
            this.addExcelHeader(head, "性别", "gender");
            this.addExcelHeader(head, "取证种类", "certType");
            this.addExcelHeader(head, "岗位", "position");
            this.addExcelHeader(head, "入职时间", "hireDate");
            this.addExcelHeader(head, "发证单位", "issuer");
            this.addExcelHeader(head, "操作证编码", "operationCode");
            this.addExcelHeader(head, "取证时间", "issueDate");
            this.addExcelHeader(head, "复审时间", "reviewDate");
            this.addExcelHeader(head, "剩余天数", "remainingDays");
            this.addExcelHeader(head, "预警状态", "alertStatus");
            
            // 导出Excel
            ExcelUtil.exportSimply(list, response, "特种作业人员证书列表");
        } catch (Exception e) {
            log.error("导出特种作业人员证书失败", e);
        }
    }
    
    /**
     * 添加Excel表头
     */
    private void addExcelHeader(List<Map<String, String>> head, String title, String dataIndex) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("title", title);
        map.put("dataIndex", dataIndex);
        head.add(map);
    }
    
    /**
     * 计算剩余天数和证书状态
     */
    private void calculateRemainingDaysAndStatus(List<SpecialOperationWorkerCert> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        
        LocalDate today = LocalDate.now();
        
        for (SpecialOperationWorkerCert cert : list) {
            // 如果没有设置复审日期，则不计算剩余天数
            if (cert.getReviewDate() == null) {
                cert.setRemainingDays(null);
                cert.setStatus("未设置");
                continue;
            }
            
            // 计算剩余天数
            long days = ChronoUnit.DAYS.between(today, cert.getReviewDate());
            cert.setRemainingDays(days);
            
            // 设置证书状态
            if (days < 0) {
                cert.setStatus("已过期");
            } else if (days <= 30) {
                cert.setStatus("即将过期");
            } else {
                cert.setStatus("有效");
            }
        }
    }

    @Override
    public R setAlert(Integer id) {
        // 参数校验
        if (id == null) {
            return R.fail("参数错误");
        }
        
        SpecialOperationWorkerCert cert = this.getById(id);
        if (cert == null) {
            return R.fail("证书不存在");
        }
        
        cert.setAlertStatus("预警中");
        cert.setAlertCount(cert.getAlertCount() + 1);
        
        boolean result = this.updateById(cert);
        return result ? R.success("设置预警成功") : R.fail("设置预警失败");
    }
    
    @Override
    public R closeAlert(Integer id) {
        // 参数校验
        if (id == null) {
            return R.fail("参数错误");
        }
        
        SpecialOperationWorkerCert cert = this.getById(id);
        if (cert == null) {
            return R.fail("证书不存在");
        }
        
        cert.setAlertStatus("未预警");
        
        boolean result = this.updateById(cert);
        return result ? R.success("关闭预警成功") : R.fail("关闭预警失败");
    }
    
    @Override
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        // 参数校验
        if (updateVo == null || updateVo.getId() == null || updateVo.getStatus() == null) {
            return R.fail("参数错误");
        }
        
        SpecialOperationWorkerCert cert = this.getById(updateVo.getId());
        if (cert == null) {
            return R.fail("证书不存在");
        }
        
        String statusDesc;
        switch (updateVo.getStatus()) {
            case "0":
                statusDesc = "未预警";
                break;
            case "1":
                statusDesc = "预警中";
                break;
            default:
                statusDesc = "未设置";
                break;
        }
        
        cert.setAlertStatus(statusDesc);
        
        boolean result = this.updateById(cert);
        return result ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }
} 