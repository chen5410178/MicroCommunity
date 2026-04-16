import request from '@/utils/request'

export function listAdminFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listAdminFee',
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