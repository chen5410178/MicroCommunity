import request from '@/utils/request'

export function finishFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.finishFee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '结束费用失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}