import request from '@/utils/request'

// 获取核销订单列表
export function listReserveGoodsConfirmOrder(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserveOrder.listReserveGoodsConfirmOrder',
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

// 核销预约订单
export function saveReserveGoodsConfirmOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserveOrder.saveReserveGoodsConfirmOrder',
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