package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.SpecialEquipmentWorkerCert;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.model.vo.search.SpecialEquipmentWorkerCertSearch;
import cn.toesbieya.jxc.service.doc.SpecialEquipmentWorkerCertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 特种设备操作作业人员证书管理Controller
 */
@RestController
@RequestMapping("admin/document/specialEquipmentWorker")
@RequiredArgsConstructor
public class SpecialEquipmentWorkerCertController {
    
    private final SpecialEquipmentWorkerCertService specialEquipmentWorkerCertService;
    
    /**
     * 分页查询
     */
    @PostMapping("search")
    public R search(@RequestBody SpecialEquipmentWorkerCertSearch search) {
        return specialEquipmentWorkerCertService.page(search);
    }
    
    /**
     * 添加证书
     */
    @PostMapping
    public R add(@Valid @RequestBody SpecialEquipmentWorkerCert cert) {
        return specialEquipmentWorkerCertService.add(cert);
    }
    
    /**
     * 更新证书
     */
    @PutMapping
    public R update(@Valid @RequestBody SpecialEquipmentWorkerCert cert) {
        return specialEquipmentWorkerCertService.update(cert);
    }
    
    /**
     * 删除证书
     */
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return specialEquipmentWorkerCertService.delete(id);
    }
    
    /**
     * 更新预警设置
     */
    @PutMapping("{id}/alert")
    public R updateAlert(@PathVariable Integer id, @RequestBody SpecialEquipmentWorkerCert cert) {
        cert.setId(id);
        return specialEquipmentWorkerCertService.updateAlert(cert);
    }
    
    /**
     * 导出证书
     */
    @PostMapping("export")
    public void export(@RequestBody SpecialEquipmentWorkerCertSearch search, HttpServletResponse response) throws Exception {
        specialEquipmentWorkerCertService.export(search, response);
    }
} 