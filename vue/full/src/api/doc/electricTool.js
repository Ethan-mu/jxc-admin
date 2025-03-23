import request from '@/api/request'

export default {
  //分页查询电动防护工器具
  search(data) {
    return request({
      url: 'admin/document/electric-tool/search',
      method: 'post',
      data
    })
  },

  //添加电动防护工器具
  add(data) {
    return request({
      url: 'admin/document/electric-tool',
      method: 'post',
      data
    })
  },

  //修改电动防护工器具
  update(data) {
    return request({
      url: 'admin/document/electric-tool',
      method: 'put',
      data
    })
  },

  //删除电动防护工器具
  del(id) {
    return request({
      url: `admin/document/electric-tool/${id}`,
      method: 'delete'
    })
  },

  //设置预警
  setAlert(id) {
    return request({
      url: `admin/document/electric-tool/alert/set/${id}`,
      method: 'put'
    })
  },

  //关闭预警
  closeAlert(id) {
    return request({
      url: `admin/document/electric-tool/alert/close/${id}`,
      method: 'put'
    })
  },

  //更新预警状态
  updateAlertStatus(data) {
    return request({
      url: 'admin/document/electric-tool/alert/status',
      method: 'put',
      data
    })
  },

  //导出电动防护工器具
  exportTool(data) {
    return request({
      url: 'admin/document/electric-tool/export',
      method: 'post',
      responseType: 'blob',
      data
    })
  }
} 