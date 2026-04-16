import request from '@/utils/request'

export function deleteParkingSpaceApply(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.deleteParkingSpaceApply',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除车位申请失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}