import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function listBuildingShops(params) {
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

export function saveBuildingShop(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.saveBuildingShop',
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
        reject(new Error(res.msg || '保存商铺失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function updateBuildingShop(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.updateBuildingShop',
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
        reject(new Error(res.msg || '更新商铺失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function deleteBuildingShop(shopId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.deleteBuildingShop',
      method: 'post',
      data: {
        shopId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除商铺失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
