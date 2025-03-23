package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.SpecialOperationWorkerCert;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SpecialOperationWorkerCertSearch;
import cn.toesbieya.jxc.service.doc.SpecialOperationWorkerCertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 特种作业人员证书管理Controller
 */
@RestController
@RequestMapping("admin/document/specialOperationWorker")
@RequiredArgsConstructor
public class SpecialOperationWorkerCertController {
    
    private final SpecialOperationWorkerCertService specialOperationWorkerCertService;
    
    /**
     * 分页查询
     */
    @PostMapping("search")
    public R search(@RequestBody SpecialOperationWorkerCertSearch search) {
        return specialOperationWorkerCertService.page(search);
    }
    
    /**
     * 添加证书
     */
    @PostMapping
    public R add(@Valid @RequestBody SpecialOperationWorkerCert cert) {
        return specialOperationWorkerCertService.add(cert);
    }
    
    /**
     * 更新证书
     */
    @PutMapping
    public R update(@Valid @RequestBody SpecialOperationWorkerCert cert) {
        return specialOperationWorkerCertService.update(cert);
    }
    
    /**
     * 删除证书
     */
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return specialOperationWorkerCertService.delete(id);
    }
    
    /**
     * 导出证书
     */
    @PostMapping("export")
    public void export(@RequestBody SpecialOperationWorkerCertSearch search, HttpServletResponse response) throws Exception {
        specialOperationWorkerCertService.export(search, response);
    }
} 