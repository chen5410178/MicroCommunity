import request from '@/utils/request'

// 获取车辆进出记录列表
export function listAdminCarInoutDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.listAdminCarInoutDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || '获取车辆进出记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理小区列表
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
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '获取管理小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}