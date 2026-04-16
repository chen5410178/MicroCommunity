import request from '@/utils/request'

export function listAdminParkingSpaceApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.listAdminParkingSpaceApply',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get parking space apply'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}