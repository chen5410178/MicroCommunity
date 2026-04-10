import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取投诉类型列表
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

// 删除投诉类型
export function deleteComplaintType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.deleteComplaintType',
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

// 添加投诉类型
export function addComplaintType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.saveComplaintType',
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

// 更新投诉类型
export function updateComplaintType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.updateComplaintType',
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

// 获取单个投诉类型详情
export function getComplaintTypeDetail(typeCd) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.getComplaintType',
      method: 'get',
      params: {
        typeCd,
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