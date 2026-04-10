import request from '@/utils/request'

export function updateUnit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/unit.updateUnit',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新单元失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}