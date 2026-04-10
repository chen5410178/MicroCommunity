import request from '@/utils/request'

// 获取采购申请列表
export function listPurchaseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApply.listPurchaseApplys',
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

// 获取供应商列表
export function listResourceSuppliers(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceSupplier.listResourceSuppliers',
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

// 提交入库申请
export function resourceEnter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchase/resourceEnter',
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