package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.SafetyInspection;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SafetyInspectionSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 安全隐患排查Service接口
 */
public interface SafetyInspectionService extends IService<SafetyInspection> {

    /**
     * 分页查询安全隐患排查
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(SafetyInspectionSearch search);

    /**
     * 新增安全隐患排查
     *
     * @param inspection 安全隐患排查信息
     * @return 操作结果
     */
    R add(SafetyInspection inspection);

    /**
     * 修改安全隐患排查
     *
     * @param inspection 安全隐患排查信息
     * @return 操作结果
     */
    R update(SafetyInspection inspection);

    /**
     * 导出安全隐患排查
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(SafetyInspectionSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 安全隐患排查ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 安全隐患排查ID
     * @return 操作结果
     */
    R closeAlert(Integer id);
    
    /**
     * 更新预警状态
     *
     * @param updateVo 更新参数
     * @return 操作结果
     */
    R updateAlertStatus(AlertStatusUpdateVo updateVo);
    
    /**
     * 删除安全隐患排查
     *
     * @param id 安全隐患排查ID
     * @return 操作结果
     */
    R delete(Integer id);
    
    /**
     * 批量删除安全隐患排查
     *
     * @param ids 安全隐患排查ID列表
     * @return 操作结果
     */
    R batchDelete(List<Integer> ids);
    
    /**
     * 获取安全隐患统计信息
     *
     * @return 统计信息
     */
    R getStatistics();
} 