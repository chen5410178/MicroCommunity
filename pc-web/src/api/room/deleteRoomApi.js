import request from '@/utils/request'

export function deleteRoom(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.deleteRoom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '删除房屋失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}