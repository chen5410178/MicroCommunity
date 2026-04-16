import request from '@/utils/request'

// 获取支付二维码列表
export function listPayFeeQrcode(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeQrcode.listPayFeeQrcode',
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

// 添加支付二维码
export function savePayFeeQrcode(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeQrcode.savePayFeeQrcode',
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

// 更新支付二维码
export function updatePayFeeQrcode(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeQrcode.updatePayFeeQrcode',
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

// 删除支付二维码
export function deletePayFeeQrcode(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeQrcode.deletePayFeeQrcode',
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