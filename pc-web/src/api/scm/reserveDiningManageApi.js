import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取预约就餐列表
export function listReserveGoods(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveGoods',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取预约目录列表
export function listReserveCatalog(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveCatalog',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取预约参数
export function listReserveParams(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.listReserveParams',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存预约订单
export function saveReserveGoodsOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserveOrder.saveReserveGoodsOrder',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除预约商品
export function deleteReserveGoods(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.deleteReserveGoods',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}