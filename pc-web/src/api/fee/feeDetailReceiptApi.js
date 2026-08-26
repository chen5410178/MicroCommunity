import request from '@/utils/request'

export function queryFeeReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeReceipt/queryFeeReceipt',
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