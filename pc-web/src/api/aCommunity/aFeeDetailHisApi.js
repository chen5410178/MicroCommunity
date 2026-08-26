import request from '@/utils/request'

export function queryAdminHisFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminHisFee',
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