import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取小区公示列表
export function getCommunityPublicityList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/publicity.listCommunityPublicity',
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

// 删除小区公示
export function deleteCommunityPublicity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/publicity.deleteCommunityPublicity',
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

// 获取字典数据
export function getDict(dictType, dictName) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType,
        dictName
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}