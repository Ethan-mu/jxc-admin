package cn.toesbieya.jxc.service.doc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.toesbieya.jxc.model.entity.EmergencySupplies;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.EmergencySuppliesSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.EmergencySuppliesExport;
import cn.toesbieya.jxc.mapper.EmergencySuppliesMapper;
import cn.toesbieya.jxc.service.doc.EmergencySuppliesService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应急物资管理Service实现类
 */
@Service
public class EmergencySuppliesServiceImpl extends ServiceImpl<EmergencySuppliesMapper, EmergencySupplies> implements EmergencySuppliesService {

    private static final Logger logger = LoggerFactory.getLogger(EmergencySuppliesServiceImpl.class);

    @Override
    public R page(EmergencySuppliesSearch search) {
        // 参数防空处理
        if (search == null) {
            search = new EmergencySuppliesSearch();
        }
        
        // 设置默认分页参数
        if (search.getPage() == null) {
            search.setPage(1);
        }
        if (search.getPageSize() == null) {
            search.setPageSize(10);
        }
        
        // 构建查询条件
        LambdaQueryWrapper<EmergencySupplies> wrapper = buildQueryWrapper(search);

        // 执行分页查询
        Page<EmergencySupplies> page = new Page<>(search.getPage(), search.getPageSize());
        page = this.page(page, wrapper);

        // 处理查询结果，计算剩余天数和状态
        List<EmergencySupplies> records = page.getRecords();
        LocalDate now = LocalDate.now();
        for (EmergencySupplies supplies : records) {
            processExpirationData(supplies, now);
        }

        return R.success(page);
    }

    @Override
    @Transactional
    public R add(EmergencySupplies supplies) {
        if (supplies == null) {
            return R.fail("参数错误");
        }
        
        // 获取当前登录用户
        UserVo currentUser = SessionUtil.get();
        if (currentUser == null) {
            return R.fail("您尚未登录或登录已过期，请重新登录");
        }
        
        // 设置用户ID
        supplies.setUid(currentUser.getId());
        
        // 设置默认值
        if (supplies.getAlertStatus() == null) {
            supplies.setAlertStatus("未设置");
        }
        if (supplies.getAlertCount() == null) {
            supplies.setAlertCount(0);
        }
        
        // 保存物资
        boolean success = save(supplies);
        
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(EmergencySupplies supplies) {
        if (supplies == null || supplies.getId() == null) {
            return R.fail("参数错误");
        }
        
        boolean success = updateById(supplies);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    public void export(EmergencySuppliesSearch search, HttpServletResponse response) {
        try {
            // 移除分页限制，获取所有符合条件的数据
            if (search == null) {
                search = new EmergencySuppliesSearch();
            }
            search.setPage(null);
            search.setPageSize(null);
            
            // 构建查询条件
            LambdaQueryWrapper<EmergencySupplies> wrapper = buildQueryWrapper(search);
            
            // 查询数据
            List<EmergencySupplies> list = this.list(wrapper);
            
            // 处理数据，计算剩余天数和状态
            LocalDate now = LocalDate.now();
            for (EmergencySupplies supplies : list) {
                processExpirationData(supplies, now);
            }
            
            // 转换为导出格式
            List<EmergencySuppliesExport> exportList = list.stream()
                    .map(this::convertToExport)
                    .collect(Collectors.toList());
            
            // 导出Excel
            ExcelUtil.exportSimply(exportList, response, "应急物资列表");
        } catch (Exception e) {
            logger.error("导出应急物资列表失败", e);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R setAlert(Integer id) {
        EmergencySupplies supplies = this.getById(id);
        if (supplies == null) {
            return R.fail("物资不存在");
        }
        
        supplies.setAlertStatus("待预警");
        if (this.updateById(supplies)) {
            return R.success("设置预警成功");
        }
        return R.fail("设置预警失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R closeAlert(Integer id) {
        EmergencySupplies supplies = this.getById(id);
        if (supplies == null) {
            return R.fail("物资不存在");
        }
        
        supplies.setAlertStatus("已关闭");
        if (this.updateById(supplies)) {
            return R.success("关闭预警成功");
        }
        return R.fail("关闭预警失败");
    }

    @Override
    @Transactional
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        if (updateVo == null || updateVo.getId() == null || StringUtils.isEmpty(updateVo.getStatus())) {
            return R.fail("参数错误");
        }

        EmergencySupplies supplies = this.getById(updateVo.getId());
        if (supplies == null) {
            return R.fail("未找到该应急物资");
        }

        supplies.setAlertStatus(updateVo.getStatus());

        // 如果是已提醒状态，增加预警次数
        if ("已提醒".equals(updateVo.getStatus())) {
            Integer alertCount = supplies.getAlertCount();
            supplies.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }

        boolean success = this.updateById(supplies);
        return success ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<EmergencySupplies> buildQueryWrapper(EmergencySuppliesSearch search) {
        LambdaQueryWrapper<EmergencySupplies> wrapper = new LambdaQueryWrapper<>();
        
        // 获取当前登录用户，添加用户ID过滤条件
        UserVo currentUser = SessionUtil.get();
        if (currentUser != null) {
            wrapper.eq(EmergencySupplies::getUid, currentUser.getId());
        }
        
        // 添加查询条件
        if (search != null) {
            wrapper.like(!StringUtils.isEmpty(search.getSupplyType()), 
                    EmergencySupplies::getSupplyType, search.getSupplyType())
                   .eq(!StringUtils.isEmpty(search.getUnit()), 
                       EmergencySupplies::getUnit, search.getUnit())
                   .eq(!StringUtils.isEmpty(search.getStatus()), 
                       EmergencySupplies::getStatus, search.getStatus())
                   .like(!StringUtils.isEmpty(search.getStorageLocation()), 
                       EmergencySupplies::getStorageLocation, search.getStorageLocation())
                   .like(!StringUtils.isEmpty(search.getManagerName()), 
                       EmergencySupplies::getManagerName, search.getManagerName())
                   .eq(!StringUtils.isEmpty(search.getAlertStatus()), 
                       EmergencySupplies::getAlertStatus, search.getAlertStatus());
            
            // 添加时间范围查询条件
            if (search.getUseDateStart() != null) {
                wrapper.ge(EmergencySupplies::getUseDate, search.getUseDateStart());
            }
            if (search.getUseDateEnd() != null) {
                wrapper.le(EmergencySupplies::getUseDate, search.getUseDateEnd());
            }
            if (search.getRecheckDateStart() != null) {
                wrapper.ge(EmergencySupplies::getRecheckDate, search.getRecheckDateStart());
            }
            if (search.getRecheckDateEnd() != null) {
                wrapper.le(EmergencySupplies::getRecheckDate, search.getRecheckDateEnd());
            }
        }
        
        // 默认按创建时间降序排序
        wrapper.orderByDesc(EmergencySupplies::getCreateTime);
        
        return wrapper;
    }
    
    /**
     * 处理物资复检数据：计算剩余天数和设置检查状态
     */
    private void processExpirationData(EmergencySupplies supplies, LocalDate now) {
        LocalDate recheckDate = supplies.getRecheckDate();
        
        // 计算剩余天数
        if (recheckDate != null) {
            long days = ChronoUnit.DAYS.between(now, recheckDate);
            supplies.setRemainingDays(days);
            
            // 设置检查状态
            if (days < 0) {
                supplies.setCheckStatus("已过期");
            } else if (days <= 30) {
                supplies.setCheckStatus("即将过期");
            } else {
                supplies.setCheckStatus("正常");
            }
        } else {
            supplies.setRemainingDays(null);
            supplies.setCheckStatus("未设置复检日期");
        }
    }
    
    /**
     * 转换为导出对象
     */
    private EmergencySuppliesExport convertToExport(EmergencySupplies supplies) {
        EmergencySuppliesExport export = new EmergencySuppliesExport();
        
        // 复制基本字段
        export.setId(supplies.getId());
        export.setUnit(supplies.getUnit());
        export.setSupplyType(supplies.getSupplyType());
        export.setQuantity(supplies.getQuantity());
        export.setStatus(supplies.getStatus());
        export.setManagerName(supplies.getManagerName());
        export.setManagerContact(supplies.getManagerContact());
        export.setLeaderName(supplies.getLeaderName());
        export.setLeaderContact(supplies.getLeaderContact());
        export.setStorageLocation(supplies.getStorageLocation());
        export.setUseDate(supplies.getUseDate() != null ? supplies.getUseDate().toString() : null);
        export.setRecheckDate(supplies.getRecheckDate() != null ? supplies.getRecheckDate().toString() : null);
        export.setRemainingDays(supplies.getRemainingDays());
        export.setCheckStatus(supplies.getCheckStatus());
        export.setAlertStatus(supplies.getAlertStatus());
        export.setAlertCount(supplies.getAlertCount());
        
        // 格式化创建时间
        if (supplies.getCreateTime() != null) {
            export.setCreateTime(supplies.getCreateTime().toString().replace("T", " "));
        }
        
        return export;
    }
} 