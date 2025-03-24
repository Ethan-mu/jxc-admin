package cn.toesbieya.jxc.service.training;

import cn.toesbieya.jxc.model.entity.FourNewTraining;
import cn.toesbieya.jxc.model.vo.search.FourNewTrainingSearch;
import cn.toesbieya.jxc.model.vo.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 四新技术培训Service接口
 */
public interface FourNewTrainingService extends IService<FourNewTraining> {

    /**
     * 分页查询四新技术培训记录
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(FourNewTrainingSearch search);

    /**
     * 新增四新技术培训记录
     *
     * @param training 培训信息
     * @return 操作结果
     */
    R add(FourNewTraining training);

    /**
     * 修改四新技术培训记录
     *
     * @param training 培训信息
     * @return 操作结果
     */
    R update(FourNewTraining training);

    /**
     * 导出四新技术培训记录
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(FourNewTrainingSearch search, HttpServletResponse response);
} 