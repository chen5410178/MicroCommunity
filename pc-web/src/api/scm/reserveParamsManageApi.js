import request from '@/utils/request'

// 获取预约参数列表
export function listReserveParams(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveParams',
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

// 添加预约参数
export function saveReserveParams(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.saveReserveParams',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新预约参数
export function updateReserveParams(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.updateReserveParams',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除预约参数
export function deleteReserveParams(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.deleteReserveParams',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新预约开放时间
export function updateReserveParamsOpenTime(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.updateReserveParamsOpenTime',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}