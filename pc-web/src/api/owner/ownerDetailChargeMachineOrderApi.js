import request from '@/utils/request'

export function getOpenApi(params) {
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
        reject(new Error(res.msg || '获取充电订单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}