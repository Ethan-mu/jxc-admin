package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.EnterpriseInsuranceMapper;
import cn.toesbieya.jxc.model.entity.EnterpriseInsurance;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.EnterpriseInsuranceExport;
import cn.toesbieya.jxc.model.vo.search.EnterpriseInsuranceSearch;
import cn.toesbieya.jxc.service.doc.EnterpriseInsuranceService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import cn.toesbieya.jxc.util.Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业保险Service实现类
 */
@Service
public class EnterpriseInsuranceServiceImpl extends ServiceImpl<EnterpriseInsuranceMapper, EnterpriseInsurance> implements EnterpriseInsuranceService {

    @Override
    public R page(EnterpriseInsuranceSearch search) {
        // 处理搜索参数
        if (search == null) {
            search = new EnterpriseInsuranceSearch();
        }
        
        // 设置默认分页参数
        Integer page = search.getPage();
        Integer pageSize = search.getPageSize();
        if (page == null) {
            page = 1;
            search.setPage(page);
        }
        if (pageSize == null) {
            pageSize = 10;
            search.setPageSize(pageSize);
        }
        
        // 构建查询条件
        QueryWrapper<EnterpriseInsurance> queryWrapper = getQueryWrapper(search);
        
        // 执行分页查询
        Page<EnterpriseInsurance> pageParam = new Page<>(page, pageSize);
        IPage<EnterpriseInsurance> iPage = this.page(pageParam, queryWrapper);
        
        // 处理结果
        List<EnterpriseInsurance> records = iPage.getRecords();
        if (records != null && !records.isEmpty()) {
            // 计算剩余天数和检查状态
            for (EnterpriseInsurance insurance : records) {
                insurance.calculateRemainingDays();
            }
            
            // 按检查状态筛选
            final String checkStatus = search.getCheckStatus();
            if (!StringUtils.isEmpty(checkStatus)) {
                List<EnterpriseInsurance> filteredList = new ArrayList<>();
                for (EnterpriseInsurance insurance : records) {
                    if (checkStatus.equals(insurance.getCheckStatus())) {
                        filteredList.add(insurance);
                    }
                }
                iPage.setRecords(filteredList);
                iPage.setTotal(filteredList.size());
            }
        }
        
        return R.success(iPage);
    }

    @Override
    public R add(EnterpriseInsurance insurance) {
        // 校验必要字段
        if (StringUtils.isEmpty(insurance.getInsuranceType())) {
            return R.fail("保险种类不能为空");
        }
        if (StringUtils.isEmpty(insurance.getCompany())) {
            return R.fail("保险公司不能为空");
        }
        if (insurance.getStartDate() == null) {
            return R.fail("保险日期不能为空");
        }
        if (insurance.getEndDate() == null) {
            return R.fail("到期日期不能为空");
        }
        
        // 设置用户ID
        UserVo user = SessionUtil.get();
        if (user == null) {
            return R.fail("请先登录");
        }
        insurance.setUid(user.getId());
        
        // 设置初始预警状态和次数
        insurance.setAlertStatus("未设置");
        insurance.setAlertCount(0);
        
        // 设置创建时间
        insurance.setCreateTime(LocalDateTime.now());
        
        // 保存数据
        boolean success = this.save(insurance);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    public R update(EnterpriseInsurance insurance) {
        // 校验ID
        if (insurance.getId() == null) {
            return R.fail("ID不能为空");
        }
        
        // 查询原数据
        EnterpriseInsurance original = this.getById(insurance.getId());
        if (original == null) {
            return R.fail("数据不存在");
        }
        
        // 保留原预警状态和次数
        insurance.setAlertStatus(original.getAlertStatus());
        insurance.setAlertCount(original.getAlertCount());
        
        // 保留原用户ID，不允许修改
        insurance.setUid(original.getUid());
        
        // 设置更新时间
        insurance.setUpdateTime(LocalDateTime.now());
        
        // 更新数据
        boolean success = this.updateById(insurance);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    public R setAlert(Integer id) {
        if (id == null) {
            return R.fail("ID不能为空");
        }
        
        EnterpriseInsurance insurance = this.getById(id);
        if (insurance == null) {
            return R.fail("数据不存在");
        }
        
        insurance.setAlertStatus("待预警");
        insurance.setUpdateTime(LocalDateTime.now());
        
        boolean success = this.updateById(insurance);
        return success ? R.success("设置预警成功") : R.fail("设置预警失败");
    }

    @Override
    public R closeAlert(Integer id) {
        if (id == null) {
            return R.fail("ID不能为空");
        }
        
        EnterpriseInsurance insurance = this.getById(id);
        if (insurance == null) {
            return R.fail("数据不存在");
        }
        
        insurance.setAlertStatus("已关闭");
        insurance.setUpdateTime(LocalDateTime.now());
        
        boolean success = this.updateById(insurance);
        return success ? R.success("关闭预警成功") : R.fail("关闭预警失败");
    }

    @Override
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        if (updateVo == null || updateVo.getId() == null || StringUtils.isEmpty(updateVo.getStatus())) {
            return R.fail("参数错误");
        }
        
        EnterpriseInsurance insurance = this.getById(updateVo.getId());
        if (insurance == null) {
            return R.fail("数据不存在");
        }
        
        // 更新预警状态
        insurance.setAlertStatus(updateVo.getStatus());
        
        // 如果是"已提醒"状态，则预警次数加1
        if ("已提醒".equals(updateVo.getStatus())) {
            Integer alertCount = insurance.getAlertCount();
            insurance.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }
        
        insurance.setUpdateTime(LocalDateTime.now());
        
        boolean success = this.updateById(insurance);
        return success ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }

    @Override
    public void export(EnterpriseInsuranceSearch search, HttpServletResponse response) {
        try {
            // 构建查询条件
            QueryWrapper<EnterpriseInsurance> queryWrapper = getQueryWrapper(search);
            
            // 查询数据
            List<EnterpriseInsurance> list = this.list(queryWrapper);
            
            // 转换为导出VO
            List<EnterpriseInsuranceExport> exportList = new ArrayList<>();
            if (list != null && !list.isEmpty()) {
                for (EnterpriseInsurance insurance : list) {
                    insurance.calculateRemainingDays();
                    
                    // 按检查状态筛选
                    if (!StringUtils.isEmpty(search.getCheckStatus()) 
                            && !search.getCheckStatus().equals(insurance.getCheckStatus())) {
                        continue;
                    }
                    
                    exportList.add(convertToExport(insurance));
                }
            }
            
            // 导出Excel
            ExcelUtil.exportSimply(exportList, response, "企业保险列表");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 构建查询条件
     *
     * @param search 查询参数
     * @return 查询条件包装器
     */
    private QueryWrapper<EnterpriseInsurance> getQueryWrapper(EnterpriseInsuranceSearch search) {
        QueryWrapper<EnterpriseInsurance> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<EnterpriseInsurance> lambda = queryWrapper.lambda();
        
        if (search == null) {
            return queryWrapper;
        }
        
        // 保险种类
        if (!StringUtils.isEmpty(search.getInsuranceType())) {
            lambda.like(EnterpriseInsurance::getInsuranceType, search.getInsuranceType());
        }
        
        // 保险公司
        if (!StringUtils.isEmpty(search.getCompany())) {
            lambda.like(EnterpriseInsurance::getCompany, search.getCompany());
        }
        
        // 保险日期范围
        if (search.getStartDateBegin() != null) {
            lambda.ge(EnterpriseInsurance::getStartDate, search.getStartDateBegin());
        }
        if (search.getStartDateEnd() != null) {
            lambda.le(EnterpriseInsurance::getStartDate, search.getStartDateEnd());
        }
        
        // 到期日期范围
        if (search.getEndDateBegin() != null) {
            lambda.ge(EnterpriseInsurance::getEndDate, search.getEndDateBegin());
        }
        if (search.getEndDateEnd() != null) {
            lambda.le(EnterpriseInsurance::getEndDate, search.getEndDateEnd());
        }
        
        // 负责人姓名
        if (!StringUtils.isEmpty(search.getManagerName())) {
            lambda.like(EnterpriseInsurance::getManagerName, search.getManagerName());
        }
        
        // 预警状态
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            lambda.eq(EnterpriseInsurance::getAlertStatus, search.getAlertStatus());
        }
        
        // 默认按ID降序排序
        lambda.orderByDesc(EnterpriseInsurance::getId);
        
        return queryWrapper;
    }
    
    /**
     * 将实体转换为导出VO
     *
     * @param insurance 企业保险实体
     * @return 导出VO
     */
    private EnterpriseInsuranceExport convertToExport(EnterpriseInsurance insurance) {
        EnterpriseInsuranceExport export = new EnterpriseInsuranceExport();
        
        // 基本字段
        export.setId(insurance.getId());
        export.setInsuranceType(insurance.getInsuranceType());
        export.setCompany(insurance.getCompany());
        export.setAmount(insurance.getAmount());
        export.setInsuredCount(insurance.getInsuredCount());
        
        // 日期字段处理
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (insurance.getStartDate() != null) {
            export.setStartDate(insurance.getStartDate().format(formatter));
        }
        if (insurance.getEndDate() != null) {
            export.setEndDate(insurance.getEndDate().format(formatter));
        }
        
        // 剩余天数处理
        if (insurance.getRemainingDays() != null) {
            if (insurance.getRemainingDays() >= 0) {
                export.setRemainingDaysStr(insurance.getRemainingDays() + "天");
            } else {
                export.setRemainingDaysStr("已超期" + Math.abs(insurance.getRemainingDays()) + "天");
            }
        }
        
        // 其他字段
        export.setCheckStatus(insurance.getCheckStatus());
        export.setManagerName(insurance.getManagerName());
        export.setManagerPhone(insurance.getManagerPhone());
        export.setAlertStatus(insurance.getAlertStatus());
        export.setAlertCount(insurance.getAlertCount());
        
        // 创建时间
        if (insurance.getCreateTime() != null) {
            export.setCreateTime(insurance.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        return export;
    }
} 