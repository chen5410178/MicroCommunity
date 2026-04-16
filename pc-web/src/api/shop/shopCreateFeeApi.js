import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function listShops(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.listBuildingShops',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取商铺列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function saveShopCreateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.saveShopCreateFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId(),
        payerObjType: '4444'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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

export function listShopFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFee',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        payerObjType: '4444'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function listShopHisFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFeeDetail',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        payerObjType: '4444'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function listShopPrePayFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prestore.listPrestoreFees',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        payerObjType: '4444'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export function saveShopPrePayFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prestore.savePrestoreFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId(),
        payerObjType: '4444'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存预付失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function listOwnerShops(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.listBuildingShops',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主商铺列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function getShopOwnerInfo(shopId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.listBuildingShopOwners',
      method: 'get',
      params: {
        shopId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取商铺业主信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function listPayFeeRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.listPayFeeRule',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取费用规则失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
