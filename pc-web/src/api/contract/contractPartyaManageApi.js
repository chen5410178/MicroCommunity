import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询合同甲方列表
export function queryContractPartya(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractPartya/queryContractPartya',
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

// 添加合同甲方
export function saveContractPartya(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractPartya/saveContractPartya',
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

// 修改合同甲方
export function updateContractPartya(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractPartya/updateContractPartya',
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

// 删除合同甲方
export function deleteContractPartya(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractPartya/deleteContractPartya',
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