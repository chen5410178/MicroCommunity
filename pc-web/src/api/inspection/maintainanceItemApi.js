import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取检查项列表
export function listMaintainanceItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.listMaintainanceItem',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加检查项
export function saveMaintainanceItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.saveMaintainanceItem',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新检查项
export function updateMaintainanceItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.updateMaintainanceItem',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除检查项
export function deleteMaintainanceItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.deleteMaintainanceItem',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}