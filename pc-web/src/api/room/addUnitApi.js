import request from '@/utils/request'

export function saveUnit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/unit.saveUnit',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加单元失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}