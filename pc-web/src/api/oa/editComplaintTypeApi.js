import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取投诉类型详情
export function getComplaintTypeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.listComplaintType',
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

// 更新投诉类型
export function updateComplaintType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaintType.updateComplaintType',
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

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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

// 获取员工信息
export function getStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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