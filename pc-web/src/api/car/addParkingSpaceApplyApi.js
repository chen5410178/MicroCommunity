import request from '@/utils/request'

// 查询业主列表
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        currentPage: params.page,
        pageSize: params.row
      })
    }).catch(error => {
      reject(error)
    })
  })
}

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