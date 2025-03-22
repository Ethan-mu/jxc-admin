package cn.toesbieya.jxc.service.doc;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.toesbieya.jxc.model.entity.Report;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.ReportSearch;
import cn.toesbieya.jxc.model.vo.R;
import javax.servlet.http.HttpServletResponse;

/**
 * 报告管理Service接口
 */
public interface ReportService extends IService<Report> {

    /**
     * 分页查询报告
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(ReportSearch search);

    /**
     * 新增报告
     *
     * @param report 报告信息
     * @return 操作结果
     */
    R add(Report report);

    /**
     * 修改报告
     *
     * @param report 报告信息
     * @return 操作结果
     */
    R update(Report report);

    /**
     * 导出报告
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(ReportSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 报告ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 报告ID
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
} 