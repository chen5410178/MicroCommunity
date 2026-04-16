import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function saveShopOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.saveBuildingShopOwner',
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
        reject(new Error(res.msg || '保存商铺业主失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function deleteShopOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/shop.deleteBuildingShopOwner',
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
        reject(new Error(res.msg || '删除商铺业主失败'))
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
