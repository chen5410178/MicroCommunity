import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存就餐商品信息
export function saveReserveGoods(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserve.saveReserveGoods',
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

// 获取就餐参数列表
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

// 上传文件
export function uploadFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile',
      method: 'post',
      data,
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

// 获取文件下载URL
export function getFileUrl(fileId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/download/getFile/file',
      method: 'get',
      params: {
        fileId,
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