import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询费用对象信息
export function listFeeObj(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeApi/listFeeObj',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}
// 预付费用规则列表
export function listPrePayFeeRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFeeRule',
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


// 预付费用列表
export function listPrePayFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFee',
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

// 计算预付费用折扣
export function computePreFeeDiscount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeDiscount.computePreFeeDiscount',
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

// 计算费用折扣
export function computeFeeDiscount(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeDiscount/computeFeeDiscount',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 计算缴费优惠券
export function computePayFeeCoupon(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/coupon.computePayFeeCoupon',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询用户账户
export function queryCommunityOwnerAccount(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/account.queryCommunityOwnerAccount',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询押金信息
export function queryFeeDeposit(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/fee.queryFeeDeposit',
      method: 'get',
      params: { ...params, communityId, state: '1400' }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 退押金
export function refundFeeDeposit(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/fee.refundFeeDeposit',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 退押金
export function applyRefundDeposit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/refundFee.applyRefundDeposit',
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

// 缴费
export function payFee(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/fee.payFee',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 扫码支付
export function qrCodePayment(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    data.subServiceCode = 'fee.payFee'
    request({
      url: '/payment.qrCodePayment',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 检查支付状态
export function checkPayFinish(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    data.subServiceCode = 'fee.payFee'
    request({
      url: '/payment.checkPayFinish',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询收据
export function queryFeeReceipt(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeReceipt/queryFeeReceipt',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询打印页面
export function listFeePrintPages(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feePrintPage.listFeePrintPage',
      method: 'get',
      params: { ...params, communityId, state: 'T' }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 业主预存账户
export function ownerPrestoreAccount(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/account/ownerPrestoreAccount',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房间信息
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/room/queryRooms',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用信息
export function listFee(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/fee/listFee',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用配置
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeConfig/listFeeConfigs',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 计算缴费积分
export function computePayFeeIntegral(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/integral/computePayFeeIntegral',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}