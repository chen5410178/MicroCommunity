import request from '@/utils/request'

// 获取缴费订单列表
export function getPayFeeOrders(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeOrder.listPayFeeOrder',
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

// 申请退预付费用
export function applyRefundPrePayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/refundFee.applyRefundPrePayFee',
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