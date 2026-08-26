import request from '@/utils/request'

// 查询停车位申请详情
export function getParkingSpaceApplyDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.listParkingSpaceApply',
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

// 审核停车位申请
export function auditParkingSpaceApply(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpaceApply.auditParkingSpaceApply',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询停车位列表
export function queryParkingSpaces(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpace.queryParkingSpaces',
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

// 查询停车场列表
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingArea.listParkingAreas',
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