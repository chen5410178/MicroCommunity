import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取费用批次列表
export function listPayFeeBatch(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeBatch.listPayFeeBatch',
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

// 申请取消费用批次
export function applyDeletePayFeeBatch(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeBatch.applyDeletePayFeeBatchCmd',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新费用批次状态
export function updatePayFeeBatch(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeBatch.updatePayFeeBatch',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}