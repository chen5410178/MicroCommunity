import request from '@/utils/request'

export function getCommunityId() {
  return localStorage.getItem('communityId')
}

export function listBuildingShops(params) {
  return request({
    url: '/shop.listBuildingShops',
    method: 'get',
    params: {
      ...params,
      communityId: getCommunityId()
    }
  })
}

export function saveBuildingShop(data) {
  return request({
    url: '/shop.saveBuildingShop',
    method: 'post',
    data: {
      ...data,
      communityId: getCommunityId()
    }
  })
}

export function updateBuildingShop(data) {
  return request({
    url: '/shop.updateBuildingShop',
    method: 'post',
    data
  })
}

export function deleteBuildingShop(shopId) {
  return request({
    url: '/shop.deleteBuildingShop',
    method: 'post',
    data: {
      shopId
    }
  })
}
