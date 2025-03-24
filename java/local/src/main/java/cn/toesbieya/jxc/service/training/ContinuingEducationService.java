package cn.toesbieya.jxc.service.training;

import cn.toesbieya.jxc.model.entity.ContinuingEducation;
import cn.toesbieya.jxc.model.vo.search.ContinuingEducationSearch;
import cn.toesbieya.jxc.model.vo.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 继续教育培训Service接口
 */
public interface ContinuingEducationService extends IService<ContinuingEducation> {

    /**
     * 分页查询继续教育培训记录
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(ContinuingEducationSearch search);

    /**
     * 新增继续教育培训记录
     *
     * @param education 培训信息
     * @return 操作结果
     */
    R add(ContinuingEducation education);

    /**
     * 修改继续教育培训记录
     *
     * @param education 培训信息
     * @return 操作结果
     */
    R update(ContinuingEducation education);

    /**
     * 导出继续教育培训记录
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(ContinuingEducationSearch search, HttpServletResponse response);
} 