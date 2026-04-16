import request from '@/utils/request'
export function saveOwnerCarMember(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.saveOwnerCarMember',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error.response.data)
    })
  })
}