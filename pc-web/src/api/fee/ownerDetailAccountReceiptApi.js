import request from '@/utils/request'

export function listAccountReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.listAccountReceipt',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}