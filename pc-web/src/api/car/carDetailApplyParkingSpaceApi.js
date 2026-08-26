import request from '@/utils/request'

export function listParkingSpaceApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.listParkingSpaceApply',
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