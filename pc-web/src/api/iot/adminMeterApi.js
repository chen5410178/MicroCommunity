import request from '@/utils/request'

// 获取水电表列表
export function listAdminMeterMachine(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.listAdminMeterMachine',
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
        reject(new Error(res.msg || '获取水电表列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取小区列表
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