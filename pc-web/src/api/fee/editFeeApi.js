import request from '@/utils/request'

export function updateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.updateFee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新费用失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}