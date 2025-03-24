import request from '@/api/request'

// 分页查询继续教育培训记录
export function search(data) {
  return request({
    url: '/admin/training/continuing-education/search',
    method: 'post',
    data
  })
}

// 添加继续教育培训记录
export function add(data) {
  return request({
    url: '/admin/training/continuing-education',
    method: 'post',
    data
  })
}

// 更新继续教育培训记录
export function update(data) {
  return request({
    url: '/admin/training/continuing-education',
    method: 'put',
    data
  })
}

// 删除继续教育培训记录
export function del(id) {
  return request({
    url: `/admin/training/continuing-education/${id}`,
    method: 'delete'
  })
}

// 导出继续教育培训记录
export function exportEducation(data) {
  return request({
    url: '/admin/training/continuing-education/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
} 