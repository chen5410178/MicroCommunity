import request from '@/utils/request'

// 查询费用项历史记录
export function queryHisFeeConfig(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryHisFeeConfig',
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

// 查询费用历史记录
export function queryHisFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryHisFee',
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

// 查询业主历史记录
export function queryHisOwner(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryHisOwner',
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

// 获取业主属性规格
export function getAttrSpec(attrType) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrSpec.listAttrSpec',
      method: 'get',
      params: { attrType }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房屋历史记录
export function queryHisRoom(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryHisRoom',
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

// 查询车辆历史记录
export function queryHisOwnerCar(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.queryHisOwnerCar',
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

// 查询合同变更计划
export function queryContractChangePlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractChangePlan',
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