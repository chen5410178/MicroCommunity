import request from '@/utils/request'

export function exportData(params) {
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
        reject(new Error(res.msg || 'Failed to export data'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query floors'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}