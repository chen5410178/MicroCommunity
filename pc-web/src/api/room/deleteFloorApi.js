import request from '@/utils/request'

export function deleteFloor(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.deleteFloor',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}