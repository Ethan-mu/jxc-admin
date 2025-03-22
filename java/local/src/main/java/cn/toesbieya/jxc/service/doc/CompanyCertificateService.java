package cn.toesbieya.jxc.service.doc;

import cn.toesbieya.jxc.model.entity.CompanyCertificate;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.CompanyCertificateSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 公司证书Service接口
 */
public interface CompanyCertificateService extends IService<CompanyCertificate> {

    /**
     * 分页查询公司证书
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(CompanyCertificateSearch search);

    /**
     * 新增公司证书
     *
     * @param certificate 公司证书信息
     * @return 操作结果
     */
    R add(CompanyCertificate certificate);

    /**
     * 修改公司证书
     *
     * @param certificate 公司证书信息
     * @return 操作结果
     */
    R update(CompanyCertificate certificate);

    /**
     * 导出公司证书
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(CompanyCertificateSearch search, HttpServletResponse response);
    
    /**
     * 设置预警
     *
     * @param id 公司证书ID
     * @return 操作结果
     */
    R setAlert(Integer id);
    
    /**
     * 关闭预警
     *
     * @param id 公司证书ID
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