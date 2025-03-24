import request from '@/api/request'

// 分页查询四新技术培训记录
export function search(data) {
  return request({
    url: '/admin/training/four-new/search',
    method: 'post',
    data
  })
}

// 添加四新技术培训记录
export function add(data) {
  return request({
    url: '/admin/training/four-new',
    method: 'post',
    data
  })
}

// 更新四新技术培训记录
export function update(data) {
  return request({
    url: '/admin/training/four-new',
    method: 'put',
    data
  })
}

// 删除四新技术培训记录
export function del(id) {
  return request({
    url: `/admin/training/four-new/${id}`,
    method: 'delete'
  })
}

// 导出四新技术培训记录
export function exportTraining(data) {
  return request({
    url: '/admin/training/four-new/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
} 