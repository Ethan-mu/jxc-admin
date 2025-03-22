package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.EnterpriseInsurance;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.EnterpriseInsuranceSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 企业保险Service接口
 */
public interface EnterpriseInsuranceService extends IService<EnterpriseInsurance> {

    /**
     * 分页查询企业保险
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(EnterpriseInsuranceSearch search);

    /**
     * 新增企业保险
     *
     * @param insurance 企业保险信息
     * @return 操作结果
     */
    R add(EnterpriseInsurance insurance);

    /**
     * 修改企业保险
     *
     * @param insurance 企业保险信息
     * @return 操作结果
     */
    R update(EnterpriseInsurance insurance);

    /**
     * 导出企业保险
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(EnterpriseInsuranceSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 企业保险ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 企业保险ID
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