import request from '@/api/request'

/**
 * 分页查询计划外培训记录
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function search(data) {
  return request({
    url: '/admin/training/unplanned-training/search',
    method: 'post',
    data
  })
}

/**
 * 添加计划外培训记录
 * @param {Object} data 培训记录信息
 * @returns {Promise}
 */
export function add(data) {
  return request({
    url: '/admin/training/unplanned-training',
    method: 'post',
    data
  })
}

/**
 * 更新计划外培训记录
 * @param {Object} data 培训记录信息
 * @returns {Promise}
 */
export function update(data) {
  return request({
    url: '/admin/training/unplanned-training',
    method: 'put',
    data
  })
}

/**
 * 删除计划外培训记录
 * @param {Number} id 记录ID
 * @returns {Promise}
 */
export function del(id) {
  return request({
    url: `/admin/training/unplanned-training/${id}`,
    method: 'delete'
  })
}

/**
 * 导出计划外培训记录
 * @param {Object} data 查询参数
 * @returns {Promise}
 */
export function exportTraining(data) {
  return request({
    url: '/admin/training/unplanned-training/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
} 