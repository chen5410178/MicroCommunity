import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 更新预订餐饮商品
export function updateReserveGoods(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/reserve.updateReserveGoods',
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

// 获取预订参数列表
export function listReserveParams(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      page: 1,
      row: 100,
      communityId: getCommunityId()
    }
    request({
      url: '/reserve.listReserveParams',
      method: 'get',
      params: Object.assign(defaultParams, params)
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取预订餐饮商品列表
export function listReserveGoods(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      page: 1,
      row: 1,
      communityId: getCommunityId()
    }
    request({
      url: '/reserve.listReserveGoods',
      method: 'get',
      params: Object.assign(defaultParams, params)
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    const formData = new FormData()
    formData.append('uploadFile', data.file)
    formData.append('communityId', getCommunityId())

    request({
      url: 'uploadFile',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}