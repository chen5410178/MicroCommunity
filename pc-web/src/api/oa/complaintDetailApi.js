import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取投诉详情
export function getComplaintDetail(params) {
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

// 获取工单流转记录
export function listComplaintEvent(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listComplaintEvent',
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

// 获取工单评价列表
export function listComplaintAppraise(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintAppraise.listComplaintAppraise',
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

// 回复工单评价
export function replyComplaintAppraise(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintAppraise.replyComplaintAppraise',
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

// 获取工单类型列表
export function listComplaintType(params) {
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