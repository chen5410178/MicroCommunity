import request from '@/utils/request'

// 获取停车场区域列表
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingArea.listParkingAreas',
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

// 获取在场车辆列表
export function listCarIns(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
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