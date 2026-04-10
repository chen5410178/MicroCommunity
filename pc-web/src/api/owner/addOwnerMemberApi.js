import request from '@/utils/request'

export function saveOwnerMember(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.saveOwnerMember',
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

export function editOwnerMember(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.editOwnerMember',
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