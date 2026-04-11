import request from '@/utils/request'

export function getCommunityId() {
  return localStorage.getItem('communityId')
}

export function listShopLocations(params) {
  return request({
    url: '/shop.listShopLocations',
    method: 'get',
    params: {
      ...params,
      communityId: getCommunityId()
    }
  })
}

export function saveShopLocation(data) {
  return request({
    url: '/shop.saveShopLocation',
    method: 'post',
    data: {
      ...data,
      communityId: getCommunityId()
    }
  })
}

export function updateShopLocation(data) {
  return request({
    url: '/shop.updateShopLocation',
    method: 'post',
    data
  })
}

export function deleteShopLocation(locationId) {
  return request({
    url: '/shop.deleteShopLocation',
    method: 'post',
    data: {
      locationId
    }
  })
}
