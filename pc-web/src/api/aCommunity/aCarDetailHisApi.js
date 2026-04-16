import request from '@/utils/request'

export function queryAdminHisOwnerCar(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.queryAdminHisOwnerCar',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get car history'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}