import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询收据信息
export function queryFeeReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeReceipt/queryFeeReceipt',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询收据详情
export function queryFeeReceiptDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeReceipt/queryFeeReceiptDetail',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询打印规格
export function queryFeePrintSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintSpec/queryFeePrintSpec',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询打印机列表
export function listMachinePrinter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listMachinePrinter',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 提交云打印
export function printPayFeeDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/print.printPayFeeDetail',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
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