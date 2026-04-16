import request from '@/utils/request'

export function listParkingSpaceApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.listParkingSpaceApply',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取车位申请列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}