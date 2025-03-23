import request from '@/api/request'

/**
 * 分页查询安全附件
 */
export function search(data) {
    return request({
        url: 'admin/document/safety-attachment/search',
        method: 'post',
        data
    })
}

/**
 * 添加安全附件
 */
export function add(data) {
    return request({
        url: 'admin/document/safety-attachment',
        method: 'post',
        data
    })
}

/**
 * 更新安全附件
 */
export function update(data) {
    return request({
        url: 'admin/document/safety-attachment',
        method: 'put',
        data
    })
}

/**
 * 删除安全附件
 */
export function del(id) {
    return request({
        url: `admin/document/safety-attachment/${id}`,
        method: 'delete'
    })
}

/**
 * 设置预警
 */
export function setAlert(id) {
    return request({
        url: `admin/document/safety-attachment/alert/set/${id}`,
        method: 'put'
    })
}

/**
 * 关闭预警
 */
export function closeAlert(id) {
    return request({
        url: `admin/document/safety-attachment/alert/close/${id}`,
        method: 'put'
    })
}

/**
 * 更新预警状态
 */
export function updateAlertStatus(data) {
    return request({
        url: 'admin/document/safety-attachment/alert/status',
        method: 'put',
        data
    })
}

/**
 * 导出安全附件
 */
export function exportAttachment(data) {
    return request({
        url: 'admin/document/safety-attachment/export',
        method: 'post',
        responseType: 'blob',
        data
    })
} 