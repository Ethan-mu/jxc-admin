import request from '@/api/request'

/**
 * 分页查询特种设备
 */
export function search(data) {
    return request({
        url: 'admin/document/specialEquipment/search',
        method: 'post',
        data
    })
}

/**
 * 添加特种设备
 */
export function add(data) {
    return request({
        url: 'admin/document/specialEquipment',
        method: 'post',
        data
    })
}

/**
 * 更新特种设备
 */
export function update(data) {
    return request({
        url: 'admin/document/specialEquipment',
        method: 'put',
        data
    })
}

/**
 * 删除特种设备
 */
export function del(id) {
    return request({
        url: `admin/document/specialEquipment/${id}`,
        method: 'delete'
    })
}

/**
 * 更新特种设备预警设置
 */
export function updateAlert(id, data) {
    return request({
        url: `admin/document/specialEquipment/${id}/alert`,
        method: 'put',
        data
    })
}

/**
 * 设置预警
 */
export function setAlert(id) {
    return request({
        url: `admin/document/specialEquipment/alert/set/${id}`,
        method: 'put'
    })
}

/**
 * 关闭预警
 */
export function closeAlert(id) {
    return request({
        url: `admin/document/specialEquipment/alert/close/${id}`,
        method: 'put'
    })
}

/**
 * 更新预警状态
 */
export function updateAlertStatus(data) {
    return request({
        url: 'admin/document/specialEquipment/alert/status',
        method: 'put',
        data
    })
}

/**
 * 导出特种设备
 */
export function exportEquipment(data) {
    return request({
        url: 'admin/document/specialEquipment/export',
        method: 'post',
        responseType: 'blob',
        data
    })
} 