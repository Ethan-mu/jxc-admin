import request from '@/api/request'

// 分页查询复岗教育培训记录
export function search(data) {
  return request({
    url: '/admin/training/return-job/search',
    method: 'post',
    data
  })
}

// 添加复岗教育培训记录
export function add(data) {
  return request({
    url: '/admin/training/return-job',
    method: 'post',
    data
  })
}

// 更新复岗教育培训记录
export function update(data) {
  return request({
    url: '/admin/training/return-job',
    method: 'put',
    data
  })
}

// 删除复岗教育培训记录
export function del(id) {
  return request({
    url: `/admin/training/return-job/${id}`,
    method: 'delete'
  })
}

// 导出复岗教育培训记录
export function exportTraining(data) {
  return request({
    url: '/admin/training/return-job/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
} 