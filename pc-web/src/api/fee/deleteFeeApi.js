import request from '@/utils/request'

export function deleteFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.deleteFee',
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