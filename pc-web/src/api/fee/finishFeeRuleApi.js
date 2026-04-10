import request from '@/utils/request'

export function updatePayFeeRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.updatePayFeeRule',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '结束费用规则失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}