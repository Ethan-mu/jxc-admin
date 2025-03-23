import request from '@/api/request'

/**
 * 分页查询安全隐患排查
 */
export function search(data) {
    return request({
        url: 'admin/document/safety-inspection/search',
        method: 'post',
        data
    })
}

/**
 * 添加安全隐患排查
 */
export function add(data) {
    return request({
        url: 'admin/document/safety-inspection',
        method: 'post',
        data
    })
}

/**
 * 更新安全隐患排查
 */
export function update(data) {
    return request({
        url: 'admin/document/safety-inspection',
        method: 'put',
        data
    })
}

/**
 * 删除安全隐患排查
 */
export function del(id) {
    return request({
        url: `admin/document/safety-inspection/delete?id=${id}`,
        method: 'delete'
    })
}

/**
 * 批量删除安全隐患排查
 */
export function batchDelete(ids) {
    return request({
        url: 'admin/document/safety-inspection/batchDelete',
        method: 'delete',
        data: ids
    })
}

/**
 * 设置预警
 */
export function setAlert(id) {
    return request({
        url: `admin/document/safety-inspection/alert/set?id=${id}`,
        method: 'put'
    })
}

/**
 * 关闭预警
 */
export function closeAlert(id) {
    return request({
        url: `admin/document/safety-inspection/alert/close?id=${id}`,
        method: 'put'
    })
}

/**
 * 更新预警状态
 */
export function updateAlertStatus(data) {
    return request({
        url: 'admin/document/safety-inspection/alert/status',
        method: 'put',
        data
    })
}

/**
 * 获取安全隐患统计信息
 */
export function getStatistics() {
    return request({
        url: 'admin/document/safety-inspection/statistics',
        method: 'get'
    })
}

/**
 * 导出安全隐患排查
 */
export function exportInspection(data) {
    return request({
        url: 'admin/document/safety-inspection/export',
        method: 'post',
        responseType: 'blob',
        data
    })
}

export default {
    search,
    add,
    update,
    del,
    batchDelete,
    setAlert,
    closeAlert,
    updateAlertStatus,
    getStatistics,
    exportInspection
} 