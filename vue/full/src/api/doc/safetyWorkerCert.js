import request from '@/api/request'

/**
 * 分页查询安全管理人员证书
 */
export function search(data) {
    return request({
        url: 'admin/document/safetyWorkerCert/search',
        method: 'post',
        data
    })
}

/**
 * 添加安全管理人员证书
 */
export function add(data) {
    return request({
        url: 'admin/document/safetyWorkerCert',
        method: 'post',
        data
    })
}

/**
 * 更新安全管理人员证书
 */
export function update(data) {
    return request({
        url: 'admin/document/safetyWorkerCert',
        method: 'put',
        data
    })
}

/**
 * 删除安全管理人员证书
 */
export function del(id) {
    return request({
        url: `admin/document/safetyWorkerCert/${id}`,
        method: 'delete'
    })
}

/**
 * 导出安全管理人员证书
 */
export function exportCert(data) {
    return request({
        url: 'admin/document/safetyWorkerCert/export',
        method: 'post',
        responseType: 'blob',
        data
    })
} 