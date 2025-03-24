package cn.toesbieya.jxc.service.training;

import cn.toesbieya.jxc.model.entity.UnplannedEducation;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.UnplannedEducationSearch;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * 计划外培训Service接口
 */
public interface UnplannedEducationService extends IService<UnplannedEducation> {
    
    /**
     * 分页查询计划外培训记录
     *
     * @param search 查询参数
     * @return 分页结果
     */
    R page(UnplannedEducationSearch search);
    
    /**
     * 新增计划外培训记录
     *
     * @param education 培训信息
     * @return 操作结果
     */
    R add(UnplannedEducation education);
    
    /**
     * 修改计划外培训记录
     *
     * @param education 培训信息
     * @return 操作结果
     */
    R update(UnplannedEducation education);
    
    /**
     * 导出计划外培训记录
     *
     * @param search 查询参数
     * @param response HTTP响应
     */
    void export(UnplannedEducationSearch search, HttpServletResponse response);
} 