import request from '@/utils/request'

export function queryHisFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryHisFee',
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