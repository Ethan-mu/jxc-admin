import request from '@/api/request'

// 分页查询三级教育培训记录
export function search(data) {
  return request({
    url: '/admin/document/three-level-training/search',
    method: 'post',
    data
  })
}

// 添加三级教育培训记录
export function add(data) {
  return request({
    url: '/admin/document/three-level-training',
    method: 'post',
    data
  })
}

// 更新三级教育培训记录
export function update(data) {
  return request({
    url: '/admin/document/three-level-training',
    method: 'put',
    data
  })
}

// 删除三级教育培训记录
export function del(id) {
  return request({
    url: `/admin/document/three-level-training/${id}`,
    method: 'delete'
  })
}

// 导出三级教育培训记录
export function exportTraining(data) {
  return request({
    url: '/admin/document/three-level-training/export',
    method: 'post',
    data,
    responseType: 'blob'
  })
} 