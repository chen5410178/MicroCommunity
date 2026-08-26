import request from '@/utils/request'

export function listAdminParkingSpaceApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.listAdminParkingSpaceApply',
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