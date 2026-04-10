import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取公众号列表
export function listSmallWeChats(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.listSmallWeChats',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.smallWeChats,
        total: res.total || 0
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加公众号
export function saveSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.saveSmallWeChat',
      method: 'post',
      data: {
        ...data,
        objId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新公众号信息
export function updateSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.updateSmallWeChat',
      method: 'post',
      data: {
        ...data,
        objId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除公众号
export function deleteSmallWeChat(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/smallWeChat.deleteSmallWeChat',
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