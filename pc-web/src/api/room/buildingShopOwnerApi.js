import request from '@/utils/request'

export function getCommunityId() {
  return localStorage.getItem('communityId')
}

export function listBuildingShopOwners(params) {
  return request({
    url: '/shop.listBuildingShopOwners',
    method: 'get',
    params: {
      ...params,
      communityId: getCommunityId()
    }
  })
}

export function saveBuildingShopOwner(data) {
  return request({
    url: '/shop.saveBuildingShopOwner',
    method: 'post',
    data: {
      ...data,
      communityId: getCommunityId()
    }
  })
}

export function updateBuildingShopOwner(data) {
  return request({
    url: '/shop.updateBuildingShopOwner',
    method: 'post',
    data
  })
}

export function deleteBuildingShopOwner(shopOwnerId) {
  return request({
    url: '/shop.deleteBuildingShopOwner',
    method: 'post',
    data: {
      shopOwnerId
    }
  })
}
