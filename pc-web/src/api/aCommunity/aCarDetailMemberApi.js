import request from '@/utils/request'

export function queryAdminOwnerCars(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwnerCars',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get owner cars'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}