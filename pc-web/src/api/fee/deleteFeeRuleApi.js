import request from '@/utils/request'

export function deletePayFeeRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.deletePayFeeRule',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}