import request from '@/utils/request'

export function listPayFeeRuleBill(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.listPayFeeRuleBill',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取账单列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}