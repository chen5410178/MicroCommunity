import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取待办投诉单列表
export function listAuditComplaints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/auditUser.listAuditComplaints',
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

// 办理投诉单
export function auditComplaint(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.auditComplaint',
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

// 获取投诉单详情
export function getComplaintDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.getComplaint',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}