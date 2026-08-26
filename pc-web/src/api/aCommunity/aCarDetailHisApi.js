import request from '@/utils/request'

export function queryAdminHisOwnerCar(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.queryAdminHisOwnerCar',
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