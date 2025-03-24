package cn.toesbieya.jxc.service.training;

import cn.toesbieya.jxc.model.entity.ReturnJobTraining;
import cn.toesbieya.jxc.model.vo.search.ReturnJobTrainingSearch;
import cn.toesbieya.jxc.model.vo.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 复岗教育培训Service接口
 */
public interface ReturnJobTrainingService extends IService<ReturnJobTraining> {

    /**
     * 分页查询复岗教育培训记录
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(ReturnJobTrainingSearch search);

    /**
     * 新增复岗教育培训记录
     *
     * @param training 培训信息
     * @return 操作结果
     */
    R add(ReturnJobTraining training);

    /**
     * 修改复岗教育培训记录
     *
     * @param training 培训信息
     * @return 操作结果
     */
    R update(ReturnJobTraining training);

    /**
     * 导出复岗教育培训记录
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(ReturnJobTrainingSearch search, HttpServletResponse response);
} 