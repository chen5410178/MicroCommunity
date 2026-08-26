import request from '@/utils/request'

// 保存车位申请
export function saveParkingSpaceApply(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.saveParkingSpaceApply',
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