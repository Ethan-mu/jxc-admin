package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.SpecialOperationWorkerCert;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SpecialOperationWorkerCertSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 特种作业人员证书Service接口
 */
public interface SpecialOperationWorkerCertService extends IService<SpecialOperationWorkerCert> {

    /**
     * 分页查询特种作业人员证书
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(SpecialOperationWorkerCertSearch search);

    /**
     * 新增特种作业人员证书
     *
     * @param cert 特种作业人员证书信息
     * @return 操作结果
     */
    R add(SpecialOperationWorkerCert cert);

    /**
     * 修改特种作业人员证书
     *
     * @param cert 特种作业人员证书信息
     * @return 操作结果
     */
    R update(SpecialOperationWorkerCert cert);

    /**
     * 导出特种作业人员证书
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(SpecialOperationWorkerCertSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 特种作业人员证书ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 特种作业人员证书ID
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
     * 删除特种作业人员证书
     *
     * @param id 特种作业人员证书ID
     * @return 操作结果
     */
    R delete(Integer id);
}