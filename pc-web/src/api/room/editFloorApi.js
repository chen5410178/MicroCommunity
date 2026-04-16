import request from '@/utils/request'

export function editFloor(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.editFloor',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}