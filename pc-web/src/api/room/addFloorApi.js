import request from '@/utils/request'

export function addFloor(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.saveFloor',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}