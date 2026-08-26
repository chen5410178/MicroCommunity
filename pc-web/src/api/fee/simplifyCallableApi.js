import request from '@/utils/request'

export function listOweFeeCallable(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.listOweFeeCallable',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}