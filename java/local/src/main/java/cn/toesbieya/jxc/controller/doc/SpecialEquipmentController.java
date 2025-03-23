package cn.toesbieya.jxc.controller.doc;

import cn.toesbieya.jxc.model.entity.SpecialEquipment;
import cn.toesbieya.jxc.model.vo.AlertStatusUpdateVo;
import cn.toesbieya.jxc.model.vo.search.SpecialEquipmentSearch;
import cn.toesbieya.jxc.model.vo.R;
import cn.toesbieya.jxc.service.doc.SpecialEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 特种设备管理Controller
 */
@RestController
@RequestMapping("admin/document/specialEquipment")
@RequiredArgsConstructor
public class SpecialEquipmentController {

    private final SpecialEquipmentService specialEquipmentService;

    /**
     * 分页查询
     */
    @PostMapping("search")
    public R search(@RequestBody SpecialEquipmentSearch search) {
        return specialEquipmentService.page(search);
    }

    /**
     * 添加设备
     */
    @PostMapping
    public R add(@Valid @RequestBody SpecialEquipment equipment) {
        return specialEquipmentService.add(equipment);
    }

    /**
     * 更新设备
     */
    @PutMapping
    public R update(@Valid @RequestBody SpecialEquipment equipment) {
        return specialEquipmentService.update(equipment);
    }

    /**
     * 删除设备
     */
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return specialEquipmentService.delete(id);
    }

    /**
     * 更新预警设置
     */
    @PutMapping("{id}/alert")
    public R updateAlert(@PathVariable Integer id, @RequestBody SpecialEquipment equipment) {
        equipment.setId(id);
        return specialEquipmentService.updateAlert(equipment);
    }

    /**
     * 导出设备
     */
    @PostMapping("export")
    public void export(@RequestBody SpecialEquipmentSearch search, HttpServletResponse response) throws Exception {
        specialEquipmentService.export(search, response);
    }
    
    /**
     * 设置预警
     */
    @PutMapping("alert/set/{id}")
    public R setAlert(@PathVariable Integer id) {
        return specialEquipmentService.setAlert(id);
    }
    
    /**
     * 关闭预警
     */
    @PutMapping("alert/close/{id}")
    public R closeAlert(@PathVariable Integer id) {
        return specialEquipmentService.closeAlert(id);
    }
    
    /**
     * 更新预警状态
     */
    @PutMapping("alert/status")
    public R updateAlertStatus(@Valid @RequestBody AlertStatusUpdateVo updateVo) {
        return specialEquipmentService.updateAlertStatus(updateVo);
    }
} 