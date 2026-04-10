import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取审批流程列表
export function listResourceAuditFlow(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.listResourceAuditFlow',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取审批流程列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加审批流程
export function saveResourceAuditFlow(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.saveResourceAuditFlow',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加审批流程失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新审批流程
export function updateResourceAuditFlow(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.updateResourceAuditFlow',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新审批流程失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除审批流程
export function deleteResourceAuditFlow(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.deleteResourceAuditFlow',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除审批流程失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 部署流程
export function deployWorkflow(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow/deployModel',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '部署流程失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}