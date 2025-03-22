package cn.toesbieya.jxc.service.doc;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.toesbieya.jxc.model.entity.SpecialEquipment;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.SpecialEquipmentSearch;
import cn.toesbieya.jxc.model.vo.R;
import javax.servlet.http.HttpServletResponse;

/**
 * 特种设备Service接口
 */
public interface SpecialEquipmentService extends IService<SpecialEquipment> {

    /**
     * 分页查询
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(SpecialEquipmentSearch search);

    /**
     * 新增
     *
     * @param equipment 设备信息
     * @return 操作结果
     */
    R add(SpecialEquipment equipment);

    /**
     * 修改
     *
     * @param equipment 设备信息
     * @return 操作结果
     */
    R update(SpecialEquipment equipment);

    /**
     * 删除
     *
     * @param id 设备ID
     * @return 操作结果
     */
    R delete(Integer id);

    /**
     * 更新预警设置
     *
     * @param equipment 设备信息
     * @return 操作结果
     */
    R updateAlert(SpecialEquipment equipment);

    /**
     * 导出
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(SpecialEquipmentSearch search, HttpServletResponse response) throws Exception;
    
    /**
     * 设置预警
     *
     * @param id 设备ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 设备ID
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