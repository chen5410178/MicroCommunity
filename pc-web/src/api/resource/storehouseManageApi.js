import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取仓库列表
export function listStorehouses(params) {
  return new Promise((resolve, reject) => {
    const baseParams = {
      communityId: getCommunityId()
    }
    
    request({
      url: '/resourceStore.listStorehouses',
      method: 'get',
      params: { ...baseParams, ...params }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || 'Failed to get warehouse list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加仓库
export function saveStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.saveStorehouse',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to add warehouse'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新仓库
export function updateStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.updateStorehouse',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update warehouse'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除仓库
export function deleteStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.deleteStorehouse',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete warehouse'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取审核流程列表
export function listResourceAuditFlow(params) {
  return new Promise((resolve, reject) => {
    const baseParams = {
      communityId: getCommunityId(),
      page: 1,
      row: 100
    }
    
    request({
      url: '/resourceStore.listResourceAuditFlow',
      method: 'get',
      params: { ...baseParams, ...params }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || 'Failed to get audit flow list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}