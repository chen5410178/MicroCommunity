import request from '@/utils/request'

// 查询小区车辆列表
export function queryAdminOwnerCars(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwnerCars',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || 'Failed to query community cars'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询小区停车位树形结构
export function queryCommunityParkingTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityParkingTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query parking tree'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}