import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function getShopDetail(params) {
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
        reject(new Error(res.msg || '获取商铺详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function queryShopsByOwner(params) {
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
