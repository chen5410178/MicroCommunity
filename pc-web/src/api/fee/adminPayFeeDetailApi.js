import request from '@/utils/request'

export function queryAdminPayFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminPayFeeDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      console.log(res)
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function exportFeeData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}