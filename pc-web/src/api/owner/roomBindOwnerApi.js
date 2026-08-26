import request from '@/utils/request'

// 查询房屋列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
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

// 房屋交房
export function sellRoom(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.sellRoom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '房屋交房失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}