import request from '@/utils/request'

export function mergePayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeSub.mergePayFee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to merge fee'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}