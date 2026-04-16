import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取小程序列表
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
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加小程序
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

// 更新小程序
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

// 删除小程序
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

// 获取小程序字典数据
export function getSmallWeChatDict(dictType, dictCode) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType,
        dictCode
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}