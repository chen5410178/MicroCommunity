import request from '@/utils/request'

export function updateRoom(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.updateRoom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '更新房屋失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function getUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/unit.queryUnits',
      method: 'get',
      params
    }).then(response => {
      const res = response.data

        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}