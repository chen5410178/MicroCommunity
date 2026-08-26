import request from '@/utils/request'

export function listPayFeeRuleBill(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.listPayFeeRuleBill',
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