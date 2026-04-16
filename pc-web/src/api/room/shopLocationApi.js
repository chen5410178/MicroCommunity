import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function listShopLocations(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.listShopLocations',
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
        reject(new Error(res.msg || '获取商铺位置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function saveShopLocation(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.saveShopLocation',
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
        reject(new Error(res.msg || '保存商铺位置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function updateShopLocation(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.updateShopLocation',
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
        reject(new Error(res.msg || '更新商铺位置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function deleteShopLocation(locationId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.deleteShopLocation',
      method: 'post',
      data: {
        locationId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除商铺位置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
