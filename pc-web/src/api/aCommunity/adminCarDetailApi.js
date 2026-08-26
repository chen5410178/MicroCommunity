import request from '@/utils/request'

// 获取车主车辆信息
export function getOwnerCarInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwnerCars',
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

// 获取车辆费用列表
export function getCarFeeList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/carFee.listCarFee',
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

// 获取车辆缴费记录
export function getCarPaymentHistory(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payment.listCarPayment',
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

// 获取车辆修改记录
export function getCarModificationHistory(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.listCarModification',
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

// 同步车牌信息
export function syncLicensePlate(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.syncLicensePlate',
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