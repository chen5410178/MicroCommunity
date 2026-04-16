import request from '@/utils/request'

export function deleteOweFeeCallable(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oweFeeCallable.deleteOweFeeCallable',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete owe fee callable'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}