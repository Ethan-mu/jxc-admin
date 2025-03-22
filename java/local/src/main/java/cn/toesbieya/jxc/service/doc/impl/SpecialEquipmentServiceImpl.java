package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.SpecialEquipmentMapper;
import cn.toesbieya.jxc.model.entity.SpecialEquipment;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.search.SpecialEquipmentSearch;
import cn.toesbieya.jxc.service.doc.SpecialEquipmentService;
import cn.toesbieya.jxc.util.ExcelUtil;
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
 * 特种设备Service实现类
 */
@Service
@RequiredArgsConstructor
public class SpecialEquipmentServiceImpl extends ServiceImpl<SpecialEquipmentMapper, SpecialEquipment> implements SpecialEquipmentService {
    
    @Override
    public R page(SpecialEquipmentSearch search) {
        // 处理分页参数，设置默认值，避免空指针异常
        int pageNum = search.getPage() != null ? search.getPage() : 1;
        int pageSize = search.getPageSize() != null ? search.getPageSize() : 10;
        
        LambdaQueryWrapper<SpecialEquipment> wrapper = getQueryWrapper(search);
        
        // 创建分页对象
        IPage<SpecialEquipment> page = new Page<>(pageNum, pageSize);
        
        // 查询数据
        page = this.page(page, wrapper);
        
        // 处理剩余天数和校验状态
        LocalDate now = LocalDate.now();
        List<SpecialEquipment> records = page.getRecords();
        if (records != null) {
            for (SpecialEquipment equipment : records) {
                processExpirationData(equipment, now);
            }
        }
        
        return R.success(page);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R add(SpecialEquipment equipment) {
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        equipment.setUid(currentUser.getId());
        
        // 设置默认的预警状态
        if (StringUtils.isEmpty(equipment.getAlertStatus())) {
            equipment.setAlertStatus("未设置");
        }
        
        // 设置默认的预警次数
        if (equipment.getAlertCount() == null) {
            equipment.setAlertCount(0);
        }
        
        // 设置默认的设备状态
        if (StringUtils.isEmpty(equipment.getStatus())) {
            equipment.setStatus("正常");
        }
        
        // 检查是否有重复的注册编号
        if (!StringUtils.isEmpty(equipment.getRegisterNo())) {
            LambdaQueryWrapper<SpecialEquipment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SpecialEquipment::getRegisterNo, equipment.getRegisterNo());
            int count = this.count(wrapper);
            if (count > 0) {
                return R.fail("注册编号已存在，请使用其他编号");
            }
        }
        
        this.save(equipment);
        return R.success("添加成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(SpecialEquipment equipment) {
        // 检查是否有重复的注册编号
        if (!StringUtils.isEmpty(equipment.getRegisterNo())) {
            LambdaQueryWrapper<SpecialEquipment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SpecialEquipment::getRegisterNo, equipment.getRegisterNo())
                   .ne(SpecialEquipment::getId, equipment.getId());
            int count = this.count(wrapper);
            if (count > 0) {
                return R.fail("注册编号已存在，请使用其他编号");
            }
        }
        
        this.updateById(equipment);
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
    public R updateAlert(SpecialEquipment equipment) {
        SpecialEquipment update = new SpecialEquipment();
        update.setId(equipment.getId());
        update.setAlertStatus(equipment.getAlertStatus());
        update.setAlertCount(equipment.getAlertCount());
        
        this.updateById(update);
        return R.success("预警设置更新成功");
    }
    
    @Override
    @Transactional
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        if (updateVo == null || updateVo.getId() == null) {
            return R.fail("参数错误");
        }
        
        SpecialEquipment equipment = this.getById(updateVo.getId());
        if (equipment == null) {
            return R.fail("数据不存在");
        }
        
        String alertStatus = updateVo.getStatus();
        
        equipment.setAlertStatus(alertStatus);
        if ("已提醒".equals(alertStatus)) {
            equipment.setAlertCount(equipment.getAlertCount() + 1);
        }
        
        boolean success = this.updateById(equipment);
        return success ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }
    
    @Override
    public void export(SpecialEquipmentSearch search, HttpServletResponse response) throws Exception {
        // 移除分页限制
        search.setPage(null);
        search.setPageSize(null);
        
        LambdaQueryWrapper<SpecialEquipment> wrapper = getQueryWrapper(search);
        
        List<SpecialEquipment> list = this.list(wrapper);
        
        // 处理剩余天数和校验状态
        LocalDate now = LocalDate.now();
        for (SpecialEquipment equipment : list) {
            processExpirationData(equipment, now);
        }
        
        // 定义Excel表头
        List<Map<String, String>> head = new ArrayList<>();
        addExcelHeader(head, "证书名称", "certificateName");
        addExcelHeader(head, "设备类型", "equipmentType");
        addExcelHeader(head, "规格", "specifications");
        addExcelHeader(head, "出厂编号", "factoryNo");
        addExcelHeader(head, "注册编号", "registerNo");
        addExcelHeader(head, "使用证书", "usageCert");
        addExcelHeader(head, "管理人员", "managerName");
        addExcelHeader(head, "管理人员联系方式", "managerContact");
        addExcelHeader(head, "主管领导", "leaderName");
        addExcelHeader(head, "领导联系方式", "leaderContact");
        addExcelHeader(head, "制造厂家", "manufacturer");
        addExcelHeader(head, "设备状态", "status");
        addExcelHeader(head, "安装位置", "installLocation");
        addExcelHeader(head, "使用日期", "useDate");
        addExcelHeader(head, "初次校验时间", "firstCheck");
        addExcelHeader(head, "最近校验时间", "lastCheck");
        addExcelHeader(head, "复检日期", "recheckDate");
        addExcelHeader(head, "剩余天数", "remainingDays");
        addExcelHeader(head, "校验状态", "checkStatus");
        addExcelHeader(head, "预警状态", "alertStatus");
        
        // 导出Excel
        ExcelUtil.exportSimply(list, response, "特种设备列表");
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
    private LambdaQueryWrapper<SpecialEquipment> getQueryWrapper(SpecialEquipmentSearch search) {
        LambdaQueryWrapper<SpecialEquipment> wrapper = new LambdaQueryWrapper<>();
        
        // 证书名称
        if (!StringUtils.isEmpty(search.getCertificateName())) {
            wrapper.like(SpecialEquipment::getCertificateName, search.getCertificateName());
        }
        
        // 设备类型
        if (!StringUtils.isEmpty(search.getEquipmentType())) {
            wrapper.eq(SpecialEquipment::getEquipmentType, search.getEquipmentType());
        }
        
        // 出厂编号
        if (!StringUtils.isEmpty(search.getFactoryNo())) {
            wrapper.like(SpecialEquipment::getFactoryNo, search.getFactoryNo());
        }
        
        // 注册编号
        if (!StringUtils.isEmpty(search.getRegisterNo())) {
            wrapper.like(SpecialEquipment::getRegisterNo, search.getRegisterNo());
        }
        
        // 设备管理人员姓名
        if (!StringUtils.isEmpty(search.getManagerName())) {
            wrapper.like(SpecialEquipment::getManagerName, search.getManagerName());
        }
        
        // 制造厂家
        if (!StringUtils.isEmpty(search.getManufacturer())) {
            wrapper.like(SpecialEquipment::getManufacturer, search.getManufacturer());
        }
        
        // 设备状态
        if (!StringUtils.isEmpty(search.getStatus())) {
            wrapper.eq(SpecialEquipment::getStatus, search.getStatus());
        }
        
        // 安装位置
        if (!StringUtils.isEmpty(search.getInstallLocation())) {
            wrapper.like(SpecialEquipment::getInstallLocation, search.getInstallLocation());
        }
        
        // 使用日期范围
        if (search.getUseDateStart() != null) {
            wrapper.ge(SpecialEquipment::getUseDate, search.getUseDateStart());
        }
        if (search.getUseDateEnd() != null) {
            wrapper.le(SpecialEquipment::getUseDate, search.getUseDateEnd());
        }
        
        // 复检日期范围
        if (search.getRecheckDateStart() != null) {
            wrapper.ge(SpecialEquipment::getRecheckDate, search.getRecheckDateStart());
        }
        if (search.getRecheckDateEnd() != null) {
            wrapper.le(SpecialEquipment::getRecheckDate, search.getRecheckDateEnd());
        }
        
        // 预警状态
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            wrapper.eq(SpecialEquipment::getAlertStatus, search.getAlertStatus());
        }
        
        // 按创建时间降序排序
        wrapper.orderByDesc(SpecialEquipment::getCreateTime);
        
        return wrapper;
    }
    
    /**
     * 处理设备校验数据：计算剩余天数和设置校验状态
     */
    private void processExpirationData(SpecialEquipment equipment, LocalDate now) {
        LocalDate recheckDate = equipment.getRecheckDate();
        
        // 计算剩余天数
        if (recheckDate != null) {
            long days = ChronoUnit.DAYS.between(now, recheckDate);
            equipment.setRemainingDays(days);
            
            // 设置校验状态
            if (days < 0) {
                equipment.setCheckStatus("已过期");
            } else if (days <= 30) {
                equipment.setCheckStatus("即将过期");
            } else {
                equipment.setCheckStatus("正常");
            }
        } else {
            equipment.setRemainingDays(null);
            equipment.setCheckStatus("未设置复检日期");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R setAlert(Integer id) {
        SpecialEquipment equipment = this.getById(id);
        if (equipment == null) {
            return R.fail("设备不存在");
        }
        
        equipment.setAlertStatus("待预警");
        if (this.updateById(equipment)) {
            return R.success("设置预警成功");
        }
        return R.fail("设置预警失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R closeAlert(Integer id) {
        SpecialEquipment equipment = this.getById(id);
        if (equipment == null) {
            return R.fail("设备不存在");
        }
        
        equipment.setAlertStatus("已关闭");
        if (this.updateById(equipment)) {
            return R.success("关闭预警成功");
        }
        return R.fail("关闭预警失败");
    }
} 