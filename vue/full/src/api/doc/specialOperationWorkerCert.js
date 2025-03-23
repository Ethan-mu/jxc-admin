import request from '@/api/request'

/**
 * 分页查询特种作业人员证书
 */
export function search(data) {
    return request({
        url: 'admin/document/specialOperationWorker/search',
        method: 'post',
        data
    })
}

/**
 * 添加特种作业人员证书
 */
export function add(data) {
    return request({
        url: 'admin/document/specialOperationWorker',
        method: 'post',
        data
    })
}

/**
 * 更新特种作业人员证书
 */
export function update(data) {
    return request({
        url: 'admin/document/specialOperationWorker',
        method: 'put',
        data
    })
}

/**
 * 删除特种作业人员证书
 */
export function del(id) {
    return request({
        url: `admin/document/specialOperationWorker/${id}`,
        method: 'delete'
    })
}

/**
 * 导出特种作业人员证书
 */
export function exportCert(data) {
    return request({
        url: 'admin/document/specialOperationWorker/export',
        method: 'post',
        responseType: 'blob',
        data
    })
} 