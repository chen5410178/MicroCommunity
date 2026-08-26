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
      resolve({
        data: res.data,
        total: res.total,
        records: res.records
      })
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
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}