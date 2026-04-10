import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取预约服务列表
export function listReserveGoods(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/reserve.listReserveGoods',
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

// 获取预约目录列表
export function listReserveCatalog(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/reserve.listReserveCatalog',
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

// 获取预约参数
export function listReserveParams(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/reserve.listReserveParams',
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

// 保存预约订单
export function saveReserveGoodsOrder(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/reserveOrder.saveReserveGoodsOrder',
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

// 删除预约服务
export function deleteReserveGoods(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/reserve.deleteReserveGoods',
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