import request from '@/utils/request'

export function listPayFeeRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.listPayFeeRule',
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