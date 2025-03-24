package cn.toesbieya.jxc.service.training;

import cn.toesbieya.jxc.model.entity.ThreeLevelTraining;
import cn.toesbieya.jxc.model.vo.search.ThreeLevelTrainingSearch;
import cn.toesbieya.jxc.model.vo.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 三级教育培训Service接口
 */
public interface ThreeLevelTrainingService extends IService<ThreeLevelTraining> {

    /**
     * 分页查询三级教育培训记录
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(ThreeLevelTrainingSearch search);

    /**
     * 新增三级教育培训记录
     *
     * @param training 培训信息
     * @return 操作结果
     */
    R add(ThreeLevelTraining training);

    /**
     * 修改三级教育培训记录
     *
     * @param training 培训信息
     * @return 操作结果
     */
    R update(ThreeLevelTraining training);

    /**
     * 导出三级教育培训记录
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(ThreeLevelTrainingSearch search, HttpServletResponse response);
} 