import request from '@/api/request'

// 分页查询报告
export function search(data) {
  return request({
    url: '/admin/document/report/search',
    method: 'post',
    data
  })
}

// 添加报告
export function add(data) {
  return request({
    url: '/admin/document/report',
    method: 'post',
    data
  })
}

// 更新报告
export function update(data) {
  return request({
    url: '/admin/document/report',
    method: 'put',
    data
  })
}

// 删除报告
export function del(id) {
  return request({
    url: `/admin/document/report/${id}`,
    method: 'delete'
  })
}

// 导出报告
export function exportReport(data) {
  return request({
    url: '/admin/document/report/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
}

// 设置预警
export function setAlert(id) {
  return request({
    url: `/admin/document/report/alert/set/${id}`,
    method: 'put'
  })
}

// 关闭预警
export function closeAlert(id) {
  return request({
    url: `/admin/document/report/alert/close/${id}`,
    method: 'put'
  })
}

// 更新预警状态
export function updateAlertStatus(data) {
  return request({
    url: '/admin/document/report/alert/status',
    method: 'put',
    data
  })
} 