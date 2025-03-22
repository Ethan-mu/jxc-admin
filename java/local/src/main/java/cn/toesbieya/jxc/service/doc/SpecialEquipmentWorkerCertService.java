package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.SpecialEquipmentWorkerCert;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SpecialEquipmentWorkerCertSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 特种设备操作作业人员证书Service接口
 */
public interface SpecialEquipmentWorkerCertService extends IService<SpecialEquipmentWorkerCert> {
    
    /**
     * 分页查询特种设备操作作业人员证书
     */
    R page(SpecialEquipmentWorkerCertSearch search);
    
    /**
     * 添加特种设备操作作业人员证书
     */
    R add(SpecialEquipmentWorkerCert cert);
    
    /**
     * 更新特种设备操作作业人员证书
     */
    R update(SpecialEquipmentWorkerCert cert);
    
    /**
     * 删除特种设备操作作业人员证书
     */
    R delete(Integer id);
    
    /**
     * 更新预警设置
     */
    R updateAlert(SpecialEquipmentWorkerCert cert);
    
    /**
     * 导出特种设备操作作业人员证书
     */
    void export(SpecialEquipmentWorkerCertSearch search, HttpServletResponse response) throws Exception;
} 