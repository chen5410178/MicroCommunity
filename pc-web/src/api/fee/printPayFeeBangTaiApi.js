import request from '@/utils/request'

// 查询缴费收据
export function queryFeeReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeReceipt/queryFeeReceipt',
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

// 查询缴费收据详情
export function queryFeeReceiptDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeReceipt/queryFeeReceiptDetail',
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

// 查询打印规范
export function queryFeePrintSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintSpec/queryFeePrintSpec',
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