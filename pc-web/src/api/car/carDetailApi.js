import request from '@/utils/request'

// Get owner car detail
export function getOwnerCarDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwnerCars',
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

// Update car information
export function updateCarInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.updateCarInfo',
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

// Get car fee list
export function getCarFeeList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryCarFee',
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

// Get car payment history
export function getCarPaymentHistory(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryCarPaymentHistory',
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