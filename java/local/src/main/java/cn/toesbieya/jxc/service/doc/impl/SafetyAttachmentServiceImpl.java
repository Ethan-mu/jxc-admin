package cn.toesbieya.jxc.service.doc.impl;

import cn.toesbieya.jxc.mapper.SafetyAttachmentMapper;
import cn.toesbieya.jxc.model.entity.SafetyAttachment;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.UserVo;
import cn.toesbieya.jxc.model.vo.export.SafetyAttachmentExport;
import cn.toesbieya.jxc.model.vo.search.SafetyAttachmentSearch;
import cn.toesbieya.jxc.service.doc.SafetyAttachmentService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 安全附件管理Service实现类
 */
@Service
public class SafetyAttachmentServiceImpl extends ServiceImpl<SafetyAttachmentMapper, SafetyAttachment> implements SafetyAttachmentService {

    @Override
    public R page(SafetyAttachmentSearch search) {
        QueryWrapper<SafetyAttachment> queryWrapper = getQueryWrapper(search);
        
        Page<SafetyAttachment> page = new Page<>(search.getPage(), search.getPageSize());
        IPage<SafetyAttachment> iPage = this.page(page, queryWrapper);
        
        List<SafetyAttachment> records = iPage.getRecords();
        if (records != null && !records.isEmpty()) {
            // 计算剩余天数和检查状态
            records.forEach(SafetyAttachment::calculateRemainingDays);
            
            // 按检查状态筛选
            if (!StringUtils.isEmpty(search.getCheckStatus())) {
                records = records.stream()
                        .filter(item -> search.getCheckStatus().equals(item.getCheckStatus()))
                        .collect(Collectors.toList());
                iPage.setRecords(records);
                iPage.setTotal(records.size());
            }
        }
        
        return R.success(iPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R add(SafetyAttachment attachment) {
        // 验证参数
        if (attachment == null) {
            return R.fail("参数错误");
        }
        
        // 设置初始预警状态
        if (StringUtils.isEmpty(attachment.getAlertStatus())) {
            attachment.setAlertStatus("未设置");
        }
        
        // 设置预警次数初始值
        if (attachment.getAlertCount() == null) {
            attachment.setAlertCount(0);
        }
        
        // 强制从会话中获取用户ID，忽略前端传递的值
        UserVo user = SessionUtil.get();
        if (user != null && user.getId() != null) {
            attachment.setUid(user.getId());
        }
        
        // 如果依然无法获取用户ID，使用一个系统默认值（比如管理员ID=1）
        // 注意：这里假设系统中ID=1的用户是管理员或系统用户
        if (attachment.getUid() == null) {
            attachment.setUid(1); // 使用默认值，避免抛出异常
        }
        
        boolean success = this.save(attachment);
        return success ? R.success("添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R uploadAndSave(MultipartFile file, SafetyAttachment attachment) {
        // 文件上传功能需要根据实际需求实现
        return R.fail("暂不支持文件上传功能");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R update(SafetyAttachment attachment) {
        // 验证参数
        if (attachment == null || attachment.getId() == null) {
            return R.fail("参数错误");
        }
        
        // 检查数据是否存在
        SafetyAttachment exist = this.getById(attachment.getId());
        if (exist == null) {
            return R.fail("数据不存在");
        }
        
        // 设置用户ID（不允许修改）
        if (exist.getUid() != null) {
            attachment.setUid(exist.getUid());
        } else {
            // 如果现有记录没有用户ID，尝试从会话获取
            UserVo user = SessionUtil.get();
            if (user != null && user.getId() != null) {
                attachment.setUid(user.getId());
            } else {
                // 如果依然无法获取，使用系统默认值
                attachment.setUid(1); // 使用默认值，避免抛出异常
            }
        }
        
        // 保留原预警状态和次数
        if (StringUtils.isEmpty(attachment.getAlertStatus())) {
            attachment.setAlertStatus(exist.getAlertStatus());
        }
        if (attachment.getAlertCount() == null) {
            attachment.setAlertCount(exist.getAlertCount());
        }
        
        boolean success = this.updateById(attachment);
        return success ? R.success("更新成功") : R.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R setAlert(Integer id) {
        SafetyAttachment attachment = this.getById(id);
        if (attachment == null) {
            return R.fail("数据不存在");
        }
        
        attachment.setAlertStatus("待预警");
        boolean success = this.updateById(attachment);
        
        return success ? R.success("设置预警成功") : R.fail("设置预警失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R closeAlert(Integer id) {
        SafetyAttachment attachment = this.getById(id);
        if (attachment == null) {
            return R.fail("数据不存在");
        }
        
        attachment.setAlertStatus("已关闭");
        boolean success = this.updateById(attachment);
        
        return success ? R.success("关闭预警成功") : R.fail("关闭预警失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateAlertStatus(AlertStatusUpdateVo updateVo) {
        if (updateVo == null || updateVo.getId() == null) {
            return R.fail("参数错误");
        }
        
        SafetyAttachment attachment = getById(updateVo.getId());
        if (attachment == null) {
            return R.fail("未找到安全附件");
        }
        
        String alertStatus = updateVo.getStatus();
        
        if (StringUtils.isEmpty(alertStatus)) {
            return R.fail("预警状态不能为空");
        }
        
        attachment.setAlertStatus(alertStatus);
        
        // 如果是已提醒状态，增加预警次数
        if ("已提醒".equals(alertStatus)) {
            Integer alertCount = attachment.getAlertCount();
            attachment.setAlertCount(alertCount == null ? 1 : alertCount + 1);
        }
        
        boolean result = updateById(attachment);
        return result ? R.success("更新预警状态成功") : R.fail("更新预警状态失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R approve(Integer id, String status, String opinion) {
        // 审批功能根据实际需求实现
        return R.fail("暂不支持审批功能");
    }

    @Override
    public void export(SafetyAttachmentSearch search, HttpServletResponse response) {
        try {
            QueryWrapper<SafetyAttachment> queryWrapper = getQueryWrapper(search);
            List<SafetyAttachment> list = this.list(queryWrapper);
            
            if (list != null && !list.isEmpty()) {
                // 计算剩余天数和检查状态
                list.forEach(SafetyAttachment::calculateRemainingDays);
                
                // 按检查状态筛选
                if (!StringUtils.isEmpty(search.getCheckStatus())) {
                    list = list.stream()
                            .filter(item -> search.getCheckStatus().equals(item.getCheckStatus()))
                            .collect(Collectors.toList());
                }
                
                // 转换为导出VO
                List<SafetyAttachmentExport> exportList = list.stream().map(this::convertToExport)
                        .collect(Collectors.toList());
                
                // 执行导出
                ExcelUtil.exportSimply(exportList, response, "安全附件列表");
            } else {
                ExcelUtil.exportSimply(new ArrayList<>(), response, "安全附件列表");
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
    private QueryWrapper<SafetyAttachment> getQueryWrapper(SafetyAttachmentSearch search) {
        QueryWrapper<SafetyAttachment> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SafetyAttachment> lambda = queryWrapper.lambda();
        
        // 附件类型
        if (!StringUtils.isEmpty(search.getAttachmentType())) {
            lambda.like(SafetyAttachment::getAttachType, search.getAttachmentType());
        }
        
        // 单位
        if (!StringUtils.isEmpty(search.getUnit())) {
            lambda.like(SafetyAttachment::getUnit, search.getUnit());
        }
        
        // 使用状态
        if (!StringUtils.isEmpty(search.getStatus())) {
            lambda.eq(SafetyAttachment::getStatus, search.getStatus());
        }
        
        // 储存位置
        if (!StringUtils.isEmpty(search.getStorageLocation())) {
            lambda.like(SafetyAttachment::getStorageLocation, search.getStorageLocation());
        }
        
        // 管理人员
        if (!StringUtils.isEmpty(search.getManagerName())) {
            lambda.like(SafetyAttachment::getManagerName, search.getManagerName());
        }
        
        // 使用日期范围
        if (search.getUseStartDate() != null) {
            lambda.ge(SafetyAttachment::getUseDate, search.getUseStartDate());
        }
        if (search.getUseEndDate() != null) {
            lambda.le(SafetyAttachment::getUseDate, search.getUseEndDate());
        }
        
        // 复检日期范围
        if (search.getRecheckStartDate() != null) {
            lambda.ge(SafetyAttachment::getRecheckDate, search.getRecheckStartDate());
        }
        if (search.getRecheckEndDate() != null) {
            lambda.le(SafetyAttachment::getRecheckDate, search.getRecheckEndDate());
        }
        
        // 默认按ID降序排序
        lambda.orderByDesc(SafetyAttachment::getId);
        
        return queryWrapper;
    }
    
    /**
     * 将实体转换为导出VO
     *
     * @param attachment 安全附件实体
     * @return 导出VO
     */
    private SafetyAttachmentExport convertToExport(SafetyAttachment attachment) {
        SafetyAttachmentExport export = new SafetyAttachmentExport();
        
        export.setId(attachment.getId());
        export.setAttachType(attachment.getAttachType());
        export.setUnit(attachment.getUnit());
        export.setQuantity(attachment.getQuantity());
        export.setStatus(attachment.getStatus());
        export.setManagerName(attachment.getManagerName());
        export.setManagerContact(attachment.getManagerContact());
        export.setLeaderName(attachment.getLeaderName());
        export.setLeaderContact(attachment.getLeaderContact());
        export.setStorageLocation(attachment.getStorageLocation());
        export.setUseDate(attachment.getUseDate());
        export.setRecheckDate(attachment.getRecheckDate());
        export.setRemainingDays(attachment.getRemainingDays());
        export.setCheckStatus(attachment.getCheckStatus());
        export.setAlertStatus(attachment.getAlertStatus());
        
        return export;
    }
    
    /**
     * 计算剩余天数
     *
     * @param attachment 安全附件实体
     */
    private void calculateRemainingDays(SafetyAttachment attachment) {
        if (attachment.getRecheckDate() != null) {
            LocalDate now = LocalDate.now();
            long days = ChronoUnit.DAYS.between(now, attachment.getRecheckDate());
            
            attachment.setRemainingDays(days);
            
            if (days < 0) {
                attachment.setCheckStatus("已过期");
            } else if (days <= 30) {
                attachment.setCheckStatus("即将过期");
            } else {
                attachment.setCheckStatus("正常");
            }
        } else {
            attachment.setRemainingDays(null);
            attachment.setCheckStatus("未设置复检日期");
        }
    }
} 