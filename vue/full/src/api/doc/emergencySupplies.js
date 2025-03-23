import request from '@/api/request'

/**
 * 分页查询应急物资
 */
export function search(data) {
    return request({
        url: 'admin/document/emergency-supplies/search',
        method: 'post',
        data
    })
}

/**
 * 添加应急物资
 */
export function add(data) {
    return request({
        url: 'admin/document/emergency-supplies',
        method: 'post',
        data
    })
}

/**
 * 更新应急物资
 */
export function update(data) {
    return request({
        url: 'admin/document/emergency-supplies',
        method: 'put',
        data
    })
}

/**
 * 删除应急物资
 */
export function del(id) {
    return request({
        url: `admin/document/emergency-supplies/${id}`,
        method: 'delete'
    })
}

/**
 * 设置预警
 */
export function setAlert(id) {
    return request({
        url: `admin/document/emergency-supplies/alert/set/${id}`,
        method: 'put'
    })
}

/**
 * 关闭预警
 */
export function closeAlert(id) {
    return request({
        url: `admin/document/emergency-supplies/alert/close/${id}`,
        method: 'put'
    })
}

/**
 * 更新预警状态
 */
export function updateAlertStatus(data) {
    return request({
        url: 'admin/document/emergency-supplies/alert/status',
        method: 'put',
        data
    })
}

/**
 * 导出应急物资
 */
export function exportSupplies(data) {
    return request({
        url: 'admin/document/emergency-supplies/export',
        method: 'post',
        responseType: 'blob',
        data
    })
} 