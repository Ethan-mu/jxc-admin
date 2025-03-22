package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.ElectricToolMapper;
import cn.toesbieya.jxc.model.entity.ElectricTool;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.ElectricToolExport;
import cn.toesbieya.jxc.model.vo.search.ElectricToolSearch;
import cn.toesbieya.jxc.service.doc.ElectricToolService;
import cn.toesbieya.jxc.util.ExcelUtil;
import cn.toesbieya.jxc.util.SessionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 电动防护工器具Service实现类
 */
@Service
public class ElectricToolServiceImpl extends ServiceImpl<ElectricToolMapper, ElectricTool> implements ElectricToolService {

    @Override
    public R page(ElectricToolSearch search) {
        // 处理搜索参数
        if (search == null) {
            search = new ElectricToolSearch();
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
        QueryWrapper<ElectricTool> queryWrapper = getQueryWrapper(search);
        
        // 执行分页查询
        Page<ElectricTool> pageParam = new Page<>(page, pageSize);
        IPage<ElectricTool> iPage = this.page(pageParam, queryWrapper);
        
        // 处理结果
        List<ElectricTool> records = iPage.getRecords();
        if (records != null && !records.isEmpty()) {
            // 计算剩余天数和检查状态
            for (ElectricTool tool : records) {
                tool.calculateRemainingDays();
            }
            
            // 按检查状态筛选
            final String checkStatus = search.getCheckStatus();
            if (!StringUtils.isEmpty(checkStatus)) {
                List<ElectricTool> filteredList = new ArrayList<>();
                for (ElectricTool tool : records) {
                    if (checkStatus.equals(tool.getCheckStatus())) {
                        filteredList.add(tool);
                    }
                }
                iPage.setRecords(filteredList);
                iPage.setTotal(filteredList.size());
            }
        }
        
        return R.success(iPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R add(ElectricTool tool) {
        // 验证参数
        if (tool == null) {
            return R.fail("参数错误");
        }
        
        // 设置初始预警状态
        if (StringUtils.isEmpty(tool.getAlertStatus())) {
            tool.setAlertStatus("未设置");
        }
        
        // 设置预警次数初始值
        if (tool.getAlertCount() == null) {
            tool.setAlertCount(0);
        }
        
        // 强制从会话中获取用户ID，忽略前端传递的值
        UserVo user = SessionUtil.get();
        if (user != null && user.getId() != null) {
            tool.setUid(user.getId());
        }
        
        // 如果依然无法获取用户ID，使用一个系统默认值（比如管理员ID=1）
        // 注意：这里假设系统中ID=1的用户是管理员或系统用户
        if (tool.getUid() == null) {
            tool.setUid(1); // 使用默认值，避免抛出异常
        }
        
        boolean success = this.save(tool);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(ElectricTool tool) {
        // 验证参数
        if (tool == null || tool.getId() == null) {
            return R.fail("参数错误");
        }
        
        // 检查数据是否存在
        ElectricTool exist = this.getById(tool.getId());
        if (exist == null) {
            return R.fail("数据不存在");
        }
        
        // 设置用户ID（不允许修改）
        if (exist.getUid() != null) {
            tool.setUid(exist.getUid());
        } else {
            // 如果现有记录没有用户ID，尝试从会话获取
            UserVo user = SessionUtil.get();
            if (user != null && user.getId() != null) {
                tool.setUid(user.getId());
            } else {
                // 如果依然无法获取，使用系统默认值
                tool.setUid(1); // 使用默认值，避免抛出异常
            }
        }
        
        // 保留原预警状态和次数
        if (StringUtils.isEmpty(tool.getAlertStatus())) {
            tool.setAlertStatus(exist.getAlertStatus());
        }
        if (tool.getAlertCount() == null) {
            tool.setAlertCount(exist.getAlertCount());
        }
        
        boolean success = this.updateById(tool);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R setAlert(Integer id) {
        ElectricTool tool = this.getById(id);
        if (tool == null) {
            return R.fail("数据不存在");
        }
        
        tool.setAlertStatus("待预警");
        boolean success = this.updateById(tool);
        
        return success ? R.success("设置预警成功") : R.fail("设置预警失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R closeAlert(Integer id) {
        ElectricTool tool = this.getById(id);
        if (tool == null) {
            return R.fail("数据不存在");
        }
        
        tool.setAlertStatus("已关闭");
        boolean success = this.updateById(tool);
        
        return success ? R.success("关闭预警成功") : R.fail("关闭预警失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        if (updateVo == null || updateVo.getId() == null) {
            return R.fail("参数错误");
        }
        
        ElectricTool tool = getById(updateVo.getId());
        if (tool == null) {
            return R.fail("数据不存在");
        }
        
        String alertStatus = updateVo.getStatus();
        
        if (StringUtils.isEmpty(alertStatus)) {
            return R.fail("预警状态不能为空");
        }
        
        tool.setAlertStatus(alertStatus);
        
        // 如果是已提醒状态，增加预警次数
        if ("已提醒".equals(alertStatus)) {
            Integer alertCount = tool.getAlertCount();
            tool.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }
        
        boolean result = updateById(tool);
        return result ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }

    @Override
    public void export(ElectricToolSearch search, HttpServletResponse response) {
        try {
            QueryWrapper<ElectricTool> queryWrapper = getQueryWrapper(search);
            List<ElectricTool> list = this.list(queryWrapper);
            
            if (list != null && !list.isEmpty()) {
                // 计算剩余天数和检查状态
                list.forEach(ElectricTool::calculateRemainingDays);
                
                // 按检查状态筛选
                if (!StringUtils.isEmpty(search.getCheckStatus())) {
                    list = list.stream()
                            .filter(item -> search.getCheckStatus().equals(item.getCheckStatus()))
                            .collect(Collectors.toList());
                }
                
                // 转换为导出VO
                List<ElectricToolExport> exportList = list.stream().map(this::convertToExport)
                        .collect(Collectors.toList());
                
                // 执行导出
                ExcelUtil.exportSimply(exportList, response, "电动防护工器具列表");
            } else {
                ExcelUtil.exportSimply(new ArrayList<>(), response, "电动防护工器具列表");
            }
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
    private QueryWrapper<ElectricTool> getQueryWrapper(ElectricToolSearch search) {
        QueryWrapper<ElectricTool> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<ElectricTool> lambda = queryWrapper.lambda();
        
        if (search == null) {
            return queryWrapper;
        }
        
        // 名称 (适配前端)
        if (!StringUtils.isEmpty(search.getName())) {
            lambda.like(ElectricTool::getCertificateName, search.getName());
        }
        // 类型 (适配前端)
        else if (!StringUtils.isEmpty(search.getType())) {
            lambda.like(ElectricTool::getToolType, search.getType());
        }
        
        // 证书名称 (后端原字段)
        if (!StringUtils.isEmpty(search.getCertificateName())) {
            lambda.like(ElectricTool::getCertificateName, search.getCertificateName());
        }
        
        // 工具类型 (后端原字段)
        if (!StringUtils.isEmpty(search.getToolType())) {
            lambda.like(ElectricTool::getToolType, search.getToolType());
        }
        
        // 规格
        if (!StringUtils.isEmpty(search.getSpecifications())) {
            lambda.like(ElectricTool::getSpecifications, search.getSpecifications());
        }
        
        // 出厂编号
        if (!StringUtils.isEmpty(search.getFactoryNo())) {
            lambda.like(ElectricTool::getFactoryNo, search.getFactoryNo());
        }
        
        // 制造厂商
        if (!StringUtils.isEmpty(search.getManufacturer())) {
            lambda.like(ElectricTool::getManufacturer, search.getManufacturer());
        }
        
        // 使用状态
        if (!StringUtils.isEmpty(search.getStatus())) {
            lambda.eq(ElectricTool::getStatus, search.getStatus());
        }
        
        // 位置
        if (!StringUtils.isEmpty(search.getLocation())) {
            lambda.like(ElectricTool::getLocation, search.getLocation());
        }
        
        // 管理人员
        if (!StringUtils.isEmpty(search.getManagerName())) {
            lambda.like(ElectricTool::getManagerName, search.getManagerName());
        }
        
        // 使用日期范围
        if (search.getUseStartDate() != null) {
            lambda.ge(ElectricTool::getUseDate, search.getUseStartDate());
        }
        if (search.getUseEndDate() != null) {
            lambda.le(ElectricTool::getUseDate, search.getUseEndDate());
        }
        
        // 复检日期范围
        if (search.getRecheckStartDate() != null) {
            lambda.ge(ElectricTool::getRecheckDate, search.getRecheckStartDate());
        }
        if (search.getRecheckEndDate() != null) {
            lambda.le(ElectricTool::getRecheckDate, search.getRecheckEndDate());
        }
        
        // 预警状态
        if (!StringUtils.isEmpty(search.getAlertStatus())) {
            lambda.eq(ElectricTool::getAlertStatus, search.getAlertStatus());
        }
        
        // 默认按ID降序排序
        lambda.orderByDesc(ElectricTool::getId);
        
        return queryWrapper;
    }
    
    /**
     * 将实体转换为导出VO
     *
     * @param tool 电动防护工器具实体
     * @return 导出VO
     */
    private ElectricToolExport convertToExport(ElectricTool tool) {
        ElectricToolExport export = new ElectricToolExport();
        
        export.setId(tool.getId());
        export.setCertificateName(tool.getCertificateName());
        export.setToolType(tool.getToolType());
        export.setSpecifications(tool.getSpecifications());
        export.setFactoryNo(tool.getFactoryNo());
        export.setManufacturer(tool.getManufacturer());
        export.setStatus(tool.getStatus());
        export.setLocation(tool.getLocation());
        export.setManagerName(tool.getManagerName());
        export.setManagerContact(tool.getManagerContact());
        export.setLeaderName(tool.getLeaderName());
        export.setLeaderContact(tool.getLeaderContact());
        export.setUseDate(tool.getUseDate());
        export.setRecheckDate(tool.getRecheckDate());
        export.setRemainingDays(tool.getRemainingDays());
        export.setCheckStatus(tool.getCheckStatus());
        export.setAlertStatus(tool.getAlertStatus());
        export.setAlertCount(tool.getAlertCount());
        
        // 前端适配字段
        export.setName(tool.getCertificateName());
        export.setType(tool.getToolType());
        // 添加简单的默认值
        export.setCount(1);  
        export.setManager(tool.getManagerName());
        export.setRemainDaysStr(tool.getRemainingDays() != null ? tool.getRemainingDays().toString() : "");
        
        // 日期字段转字符串
        if (tool.getUseDate() != null) {
            export.setLastCheckTime(tool.getUseDate().toString());
        }
        
        if (tool.getRecheckDate() != null) {
            export.setNextCheckTime(tool.getRecheckDate().toString());
        }
        
        // 格式化创建时间
        if (tool.getCreateTime() != null) {
            export.setCreateTime(tool.getCreateTime().toString());
        }
        
        return export;
    }
} 