package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.SafetyWorkerCert;
import cn.toesbieya.jxc.model.vo.search.SafetyWorkerCertSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.doc.SafetyWorkerCertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 安全管理人员证书管理Controller
 */
@RestController
@RequestMapping("admin/document/safetyWorkerCert")
public class SafetyWorkerCertController {

    @Autowired
    private SafetyWorkerCertService safetyWorkerCertService;

    /**
     * 分页查询证书
     */
    @PostMapping("search")
    public R search(@RequestBody SafetyWorkerCertSearch search) {
        return safetyWorkerCertService.page(search);
    }

    /**
     * 新增证书
     */
    @PostMapping
    public R add(@Valid @RequestBody SafetyWorkerCert cert) {
        return safetyWorkerCertService.add(cert);
    }

    /**
     * 修改证书
     */
    @PutMapping
    public R update(@Valid @RequestBody SafetyWorkerCert cert) {
        return safetyWorkerCertService.update(cert);
    }

    /**
     * 删除证书
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        return safetyWorkerCertService.delete(id);
    }

    /**
     * 导出证书
     */
    @PostMapping("export")
    public void export(@RequestBody SafetyWorkerCertSearch search, HttpServletResponse response) throws Exception {
        safetyWorkerCertService.export(search, response);
    }
} 