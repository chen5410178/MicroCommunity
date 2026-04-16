import request from '@/utils/request'

export function getCarInoutDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取车辆进出记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}