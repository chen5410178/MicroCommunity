import request from '@/utils/request'

// 获取访客列表
export function listVisits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        iotApiCode: 'listVisitBmoImpl'
      }
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

// 获取访客类型列表
export function listVisitTypes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        iotApiCode: 'listVisitTypeBmoImpl'
      }
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