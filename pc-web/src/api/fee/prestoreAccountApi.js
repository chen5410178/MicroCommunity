import request from '@/utils/request'

export function ownerPrestoreAccount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/account.ownerPrestoreAccount',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwners',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function queryRoomsByOwner(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRoomsByOwner',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}