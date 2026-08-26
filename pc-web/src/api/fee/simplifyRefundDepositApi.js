import request from '@/utils/request'

export function queryFeeDeposit(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDeposit',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}