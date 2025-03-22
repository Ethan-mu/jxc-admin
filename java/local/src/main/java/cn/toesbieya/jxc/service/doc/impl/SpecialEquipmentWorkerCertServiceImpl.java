package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.SpecialEquipmentWorkerCertMapper;
import cn.toesbieya.jxc.model.entity.SpecialEquipmentWorkerCert;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.search.SpecialEquipmentWorkerCertSearch;
import cn.toesbieya.jxc.service.doc.SpecialEquipmentWorkerCertService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.RedisUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 特种设备操作作业人员证书Service实现类
 */
@Service
@RequiredArgsConstructor
public class SpecialEquipmentWorkerCertServiceImpl extends ServiceImpl<SpecialEquipmentWorkerCertMapper, SpecialEquipmentWorkerCert> implements SpecialEquipmentWorkerCertService {
    
    private final RedisUtil redisUtil;
    
    @Override
    public R page(SpecialEquipmentWorkerCertSearch search) {
        LambdaQueryWrapper<SpecialEquipmentWorkerCert> wrapper = getQueryWrapper(search);
        
        IPage<SpecialEquipmentWorkerCert> page = new Page<>(search.getPage(), search.getPageSize());
        
        // 查询数据
        page = this.page(page, wrapper);
        
        // 处理剩余天数和证书状态
        LocalDate now = LocalDate.now();
        List<SpecialEquipmentWorkerCert> records = page.getRecords();
        for (SpecialEquipmentWorkerCert cert : records) {
            processExpirationData(cert, now);
        }
        
        return R.success(page);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R add(SpecialEquipmentWorkerCert cert) {
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        cert.setUid(currentUser.getId());
        
        // 设置默认的预警状态
        if (StringUtils.isEmpty(cert.getAlertStatus())) {
            cert.setAlertStatus("未设置");
        }
        
        // 设置默认的预警次数
        if (cert.getAlertCount() == null) {
            cert.setAlertCount(0);
        }
        
        this.save(cert);
        return R.success("添加成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(SpecialEquipmentWorkerCert cert) {
        this.updateById(cert);
        return R.success("更新成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R delete(Integer id) {
        this.removeById(id);
        return R.success("删除成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateAlert(SpecialEquipmentWorkerCert cert) {
        SpecialEquipmentWorkerCert update = new SpecialEquipmentWorkerCert();
        update.setId(cert.getId());
        update.setAlertStatus(cert.getAlertStatus());
        update.setAlertCount(cert.getAlertCount());
        
        this.updateById(update);
        return R.success("预警设置更新成功");
    }
    
    @Override
    public void export(SpecialEquipmentWorkerCertSearch search, HttpServletResponse response) throws Exception {
        // 移除分页限制
        search.setPage(null);
        search.setPageSize(null);
        
        LambdaQueryWrapper<SpecialEquipmentWorkerCert> wrapper = getQueryWrapper(search);
        
        List<SpecialEquipmentWorkerCert> list = this.list(wrapper);
        
        // 处理剩余天数和证书状态
        LocalDate now = LocalDate.now();
        for (SpecialEquipmentWorkerCert cert : list) {
            processExpirationData(cert, now);
        }
        
        // 定义Excel表头
        List<Map<String, String>> head = new ArrayList<>();
        addExcelHeader(head, "证书名称", "certificateName");
        addExcelHeader(head, "持证人姓名", "workerName");
        addExcelHeader(head, "联系方式", "workerContact");
        addExcelHeader(head, "身份证号", "idCard");
        addExcelHeader(head, "性别", "gender");
        addExcelHeader(head, "取证种类", "certType");
        addExcelHeader(head, "岗位", "position");
        addExcelHeader(head, "入职时间", "hireDate");
        addExcelHeader(head, "发证单位", "issuer");
        addExcelHeader(head, "操作证编码", "operationCode");
        addExcelHeader(head, "取证时间", "issueDate");
        addExcelHeader(head, "复审时间", "reviewDate");
        addExcelHeader(head, "剩余天数", "remainingDays");
        addExcelHeader(head, "预警状态", "alertStatus");
        
        // 导出Excel
        ExcelUtil.exportSimply(list, response, "特种设备操作作业人员证书列表");
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
     * 获取查询条件
     */
    private LambdaQueryWrapper<SpecialEquipmentWorkerCert> getQueryWrapper(SpecialEquipmentWorkerCertSearch search) {
        LambdaQueryWrapper<SpecialEquipmentWorkerCert> wrapper = new LambdaQueryWrapper<>();
        
        // 证书名称
        if (!StringUtils.isEmpty(search.getCertificateName())) {
            wrapper.like(SpecialEquipmentWorkerCert::getCertificateName, search.getCertificateName());
        }
        
        // 持证人员姓名
        if (!StringUtils.isEmpty(search.getWorkerName())) {
            wrapper.like(SpecialEquipmentWorkerCert::getWorkerName, search.getWorkerName());
        }
        
        // 身份证号码
        if (!StringUtils.isEmpty(search.getIdCard())) {
            wrapper.like(SpecialEquipmentWorkerCert::getIdCard, search.getIdCard());
        }
        
        // 取证种类
        if (!StringUtils.isEmpty(search.getCertType())) {
            wrapper.eq(SpecialEquipmentWorkerCert::getCertType, search.getCertType());
        }
        
        // 发证单位
        if (!StringUtils.isEmpty(search.getIssuer())) {
            wrapper.like(SpecialEquipmentWorkerCert::getIssuer, search.getIssuer());
        }
        
        // 取证时间范围
        if (search.getIssueDateStart() != null) {
            wrapper.ge(SpecialEquipmentWorkerCert::getIssueDate, search.getIssueDateStart());
        }
        if (search.getIssueDateEnd() != null) {
            wrapper.le(SpecialEquipmentWorkerCert::getIssueDate, search.getIssueDateEnd());
        }
        
        // 复审时间范围
        if (search.getReviewDateStart() != null) {
            wrapper.ge(SpecialEquipmentWorkerCert::getReviewDate, search.getReviewDateStart());
        }
        if (search.getReviewDateEnd() != null) {
            wrapper.le(SpecialEquipmentWorkerCert::getReviewDate, search.getReviewDateEnd());
        }
        
        // 预警状态
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            wrapper.eq(SpecialEquipmentWorkerCert::getAlertStatus, search.getAlertStatus());
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(SpecialEquipmentWorkerCert::getCreateTime);
        
        return wrapper;
    }
    
    /**
     * 处理证书过期数据：计算剩余天数和设置证书状态
     */
    private void processExpirationData(SpecialEquipmentWorkerCert cert, LocalDate now) {
        LocalDate reviewDate = cert.getReviewDate();
        
        // 计算剩余天数
        if (reviewDate != null) {
            long days = ChronoUnit.DAYS.between(now, reviewDate);
            cert.setRemainingDays(days);
            
            // 设置证书状态
            if (days < 0) {
                cert.setStatus("已过期");
            } else if (days <= 30) {
                cert.setStatus("即将过期");
            } else {
                cert.setStatus("有效");
            }
        } else {
            cert.setRemainingDays(null);
            cert.setStatus("未设置复审时间");
        }
    }
} 