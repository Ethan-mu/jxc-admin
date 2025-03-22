package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.ElectricTool;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.ElectricToolSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 电动防护工器具Service接口
 */
public interface ElectricToolService extends IService<ElectricTool> {
    
    /**
     * 分页查询电动防护工器具
     *
     * @param search 查询条件
     * @return 分页结果
     */
    R page(ElectricToolSearch search);
    
    /**
     * 新增电动防护工器具
     *
     * @param tool 电动防护工器具信息
     * @return 操作结果
     */
    R add(ElectricTool tool);
    
    /**
     * 更新电动防护工器具
     *
     * @param tool 电动防护工器具信息
     * @return 操作结果
     */
    R update(ElectricTool tool);
    
    /**
     * 设置预警
     *
     * @param id 电动防护工器具ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 电动防护工器具ID
     * @return 操作结果
     */
    R closeAlert(Integer id);
    
    /**
     * 更新预警状态
     *
     * @param updateVo 预警状态更新VO
     * @return 操作结果
     */
    R updateAlertStatus(AlertStatusUpdateVo updateVo);
    
    /**
     * 导出电动防护工器具
     *
     * @param search   查询条件
     * @param response HttpServletResponse
     */
    void export(ElectricToolSearch search, HttpServletResponse response);
} 