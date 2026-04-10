import request from '@/utils/request'

export function updateParkingSpaceApply(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.updateParkingSpaceApply',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新车位申请失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}