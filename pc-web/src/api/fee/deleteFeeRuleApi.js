import request from '@/utils/request'

export function deletePayFeeRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.deletePayFeeRule',
      method: 'post',
      data
    }).then(response => {
      const res = response.data

        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}