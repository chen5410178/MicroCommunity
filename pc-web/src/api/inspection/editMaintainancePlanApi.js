import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取保养标准列表
export function listMaintainanceStandard(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.listMaintainanceStandard',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取保养计划列表
export function listMaintainancePlan(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.listMaintainancePlan',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取保养计划员工列表
export function listMaintainancePlanStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.listMaintainancePlanStaff',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新保养计划
export function updateMaintainancePlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.updateMaintainancePlan',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
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
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
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
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}