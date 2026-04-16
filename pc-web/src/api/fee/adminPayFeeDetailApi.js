import request from '@/utils/request'

// 查询缴费明细列表
export function queryAdminPayFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminPayFeeDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      console.log(res)
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询缴费明细失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出缴费明细
export function exportFeeData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '导出缴费明细失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}