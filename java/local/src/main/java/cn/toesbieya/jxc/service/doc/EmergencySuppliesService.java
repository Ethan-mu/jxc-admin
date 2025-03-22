package cn.toesbieya.jxc.service.doc;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.toesbieya.jxc.model.entity.EmergencySupplies;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.EmergencySuppliesSearch;
import cn.toesbieya.jxc.model.vo.R;
import javax.servlet.http.HttpServletResponse;

/**
 * 应急物资管理Service接口
 */
public interface EmergencySuppliesService extends IService<EmergencySupplies> {

    /**
     * 分页查询应急物资
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(EmergencySuppliesSearch search);

    /**
     * 新增应急物资
     *
     * @param supplies 物资信息
     * @return 操作结果
     */
    R add(EmergencySupplies supplies);

    /**
     * 修改应急物资
     *
     * @param supplies 物资信息
     * @return 操作结果
     */
    R update(EmergencySupplies supplies);

    /**
     * 导出应急物资
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(EmergencySuppliesSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 物资ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 物资ID
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