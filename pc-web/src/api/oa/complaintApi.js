import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取投诉列表
export function listComplaints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listComplaints',
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

// 获取投诉类型列表
export function listComplaintTypes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.listComplaintType',
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

// 更新投诉信息
export function updateComplaint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.updateComplaint',
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

// 删除投诉
export function deleteComplaint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.deleteComplaint',
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

// 获取工作流审核信息
export function listWorkflowAuditInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflowAuditInfo',
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