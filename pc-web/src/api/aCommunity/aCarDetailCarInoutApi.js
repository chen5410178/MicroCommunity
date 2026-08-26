import request from '@/utils/request'

export function getCarInoutDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getAdminOpenApi',
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