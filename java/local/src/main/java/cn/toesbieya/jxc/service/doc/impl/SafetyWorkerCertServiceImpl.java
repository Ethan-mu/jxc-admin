package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.SafetyWorkerCertMapper;
import cn.toesbieya.jxc.model.entity.SafetyWorkerCert;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SafetyWorkerCertSearch;
import cn.toesbieya.jxc.service.doc.SafetyWorkerCertService;
import cn.toesbieya.jxc.util.SessionUtil;
import cn.toesbieya.jxc.util.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;

/**
 * 安全管理人员证书管理Service实现
 */
@Service
@Slf4j
public class SafetyWorkerCertServiceImpl extends ServiceImpl<SafetyWorkerCertMapper, SafetyWorkerCert> implements SafetyWorkerCertService {

    @Override
    public R page(SafetyWorkerCertSearch search) {
        LambdaQueryWrapper<SafetyWorkerCert> wrapper = new QueryWrapper<SafetyWorkerCert>().lambda();
        
        // 添加查询条件
        if (!StringUtils.isEmpty(search.getCertificateName())) {
            wrapper.like(SafetyWorkerCert::getCertificateName, search.getCertificateName());
        }
        if (!StringUtils.isEmpty(search.getHolderName())) {
            wrapper.like(SafetyWorkerCert::getHolderName, search.getHolderName());
        }
        if (!StringUtils.isEmpty(search.getIdCard())) {
            wrapper.like(SafetyWorkerCert::getIdCard, search.getIdCard());
        }
        if (!StringUtils.isEmpty(search.getCertType())) {
            wrapper.eq(SafetyWorkerCert::getCertType, search.getCertType());
        }
        if (!StringUtils.isEmpty(search.getIssuer())) {
            wrapper.like(SafetyWorkerCert::getIssuer, search.getIssuer());
        }
        if (search.getIssueDateStart() != null) {
            wrapper.ge(SafetyWorkerCert::getIssueDate, search.getIssueDateStart());
        }
        if (search.getIssueDateEnd() != null) {
            wrapper.le(SafetyWorkerCert::getIssueDate, search.getIssueDateEnd());
        }
        if (search.getReviewDateStart() != null) {
            wrapper.ge(SafetyWorkerCert::getReviewDate, search.getReviewDateStart());
        }
        if (search.getReviewDateEnd() != null) {
            wrapper.le(SafetyWorkerCert::getReviewDate, search.getReviewDateEnd());
        }
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            wrapper.eq(SafetyWorkerCert::getAlertStatus, search.getAlertStatus());
        }
        
        // 分页查询
        IPage<SafetyWorkerCert> page = new Page<>(search.getPage(), search.getPageSize());
        page = page(page, wrapper);
        
        // 计算剩余天数和状态
        calculateRemainingDaysAndStatus(page.getRecords());
        
        return R.success(page);
    }

    @Override
    public R add(SafetyWorkerCert cert) {
        // 设置当前用户ID
        cert.setUid(SessionUtil.get().getId());
        
        // 保存数据
        boolean success = save(cert);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(SafetyWorkerCert cert) {
        // 验证记录存在
        SafetyWorkerCert existCert = getById(cert.getId());
        if (existCert == null) {
            return R.fail("证书不存在");
        }
        
        // 保存数据
        boolean success = updateById(cert);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    public R delete(Integer id) {
        boolean success = removeById(id);
        return success ? R.success("删除成功") : R.fail("删除失败");
    }

    @Override
    public void export(SafetyWorkerCertSearch search, HttpServletResponse response) {
        try {
            // 查询数据
            LambdaQueryWrapper<SafetyWorkerCert> wrapper = new QueryWrapper<SafetyWorkerCert>().lambda();
            
            // 添加查询条件
            if (!StringUtils.isEmpty(search.getCertificateName())) {
                wrapper.like(SafetyWorkerCert::getCertificateName, search.getCertificateName());
            }
            if (!StringUtils.isEmpty(search.getHolderName())) {
                wrapper.like(SafetyWorkerCert::getHolderName, search.getHolderName());
            }
            if (!StringUtils.isEmpty(search.getIdCard())) {
                wrapper.like(SafetyWorkerCert::getIdCard, search.getIdCard());
            }
            if (!StringUtils.isEmpty(search.getCertType())) {
                wrapper.eq(SafetyWorkerCert::getCertType, search.getCertType());
            }
            if (!StringUtils.isEmpty(search.getIssuer())) {
                wrapper.like(SafetyWorkerCert::getIssuer, search.getIssuer());
            }
            if (search.getIssueDateStart() != null) {
                wrapper.ge(SafetyWorkerCert::getIssueDate, search.getIssueDateStart());
            }
            if (search.getIssueDateEnd() != null) {
                wrapper.le(SafetyWorkerCert::getIssueDate, search.getIssueDateEnd());
            }
            if (search.getReviewDateStart() != null) {
                wrapper.ge(SafetyWorkerCert::getReviewDate, search.getReviewDateStart());
            }
            if (search.getReviewDateEnd() != null) {
                wrapper.le(SafetyWorkerCert::getReviewDate, search.getReviewDateEnd());
            }
            if (!StringUtils.isEmpty(search.getAlertStatus())) {
                wrapper.eq(SafetyWorkerCert::getAlertStatus, search.getAlertStatus());
            }
            
            List<SafetyWorkerCert> list = list(wrapper);
            
            // 计算剩余天数和状态
            calculateRemainingDaysAndStatus(list);
            
            // 导出Excel
            ExcelUtil.exportSimply(list, response, "安全管理人员证书列表");
        } catch (Exception e) {
            log.error("导出安全工作人员证书失败", e);
        }
    }
    
    /**
     * 计算剩余天数和状态
     */
    private void calculateRemainingDaysAndStatus(List<SafetyWorkerCert> list) {
        LocalDate today = LocalDate.now();
        for (SafetyWorkerCert cert : list) {
            if (cert.getReviewDate() != null) {
                long days = ChronoUnit.DAYS.between(today, cert.getReviewDate());
                cert.setRemainingDays(days);
                
                // 设置状态
                if (days < 0) {
                    cert.setStatus("已过期");
                } else if (days <= 30) {
                    cert.setStatus("即将到期");
                } else {
                    cert.setStatus("有效");
                }
            }
        }
    }

    @Override
    public R closeAlert(Integer id) {
        if (id == null) {
            return R.fail("参数错误");
        }
        
        SafetyWorkerCert cert = this.getById(id);
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
        if (updateVo == null || updateVo.getId() == null) {
            return R.fail("参数错误");
        }
        
        SafetyWorkerCert cert = this.getById(updateVo.getId());
        if (cert == null) {
            return R.fail("证书不存在");
        }
        
        // 更新预警状态
        if (updateVo.getStatus() != null) {
            switch (updateVo.getStatus()) {
                case "0":
                    cert.setAlertStatus("未预警");
                    break;
                case "1":
                    cert.setAlertStatus("预警中");
                    break;
                default:
                    cert.setAlertStatus("未设置");
                    break;
            }
        }
        
        boolean result = this.updateById(cert);
        return result ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }

    @Override
    public R setAlert(Integer id) {
        if (id == null) {
            return R.fail("参数错误");
        }
        
        SafetyWorkerCert cert = this.getById(id);
        if (cert == null) {
            return R.fail("证书不存在");
        }
        
        cert.setAlertStatus("预警中");
        
        boolean result = this.updateById(cert);
        return result ? R.success("设置预警成功") : R.fail("设置预警失败");
    }
} 