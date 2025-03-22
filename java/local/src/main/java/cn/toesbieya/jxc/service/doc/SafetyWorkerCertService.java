package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.SafetyWorkerCert;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SafetyWorkerCertSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 安全工作人员证书Service接口
 */
public interface SafetyWorkerCertService extends IService<SafetyWorkerCert> {

    /**
     * 分页查询安全工作人员证书
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(SafetyWorkerCertSearch search);

    /**
     * 新增安全工作人员证书
     *
     * @param cert 安全工作人员证书信息
     * @return 操作结果
     */
    R add(SafetyWorkerCert cert);

    /**
     * 修改安全工作人员证书
     *
     * @param cert 安全工作人员证书信息
     * @return 操作结果
     */
    R update(SafetyWorkerCert cert);

    /**
     * 导出安全工作人员证书
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(SafetyWorkerCertSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 安全工作人员证书ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 安全工作人员证书ID
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
     * 删除安全工作人员证书
     *
     * @param id 安全工作人员证书ID
     * @return 操作结果
     */
    R delete(Integer id);
} 