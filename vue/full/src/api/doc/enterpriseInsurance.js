import request from '@/api/request'

// 分页查询企业保险
export function search(data) {
  return request({
    url: '/admin/document/enterprise-insurance/search',
    method: 'post',
    data
  })
}

// 添加企业保险
export function add(data) {
  return request({
    url: '/admin/document/enterprise-insurance',
    method: 'post',
    data
  })
}

// 更新企业保险
export function update(data) {
  return request({
    url: '/admin/document/enterprise-insurance',
    method: 'put',
    data
  })
}

// 删除企业保险
export function del(id) {
  return request({
    url: `/admin/document/enterprise-insurance/${id}`,
    method: 'delete'
  })
}

// 设置预警
export function setAlert(id) {
  return request({
    url: `/admin/document/enterprise-insurance/alert/set/${id}`,
    method: 'put'
  })
}

// 关闭预警
export function closeAlert(id) {
  return request({
    url: `/admin/document/enterprise-insurance/alert/close/${id}`,
    method: 'put'
  })
}

// 更新预警状态
export function updateAlertStatus(data) {
  return request({
    url: '/admin/document/enterprise-insurance/alert/status',
    method: 'put',
    data
  })
}

// 导出企业保险
export function exportInsurance(data) {
  return request({
    url: '/admin/document/enterprise-insurance/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
} 