import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询合同列表
export function queryContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房间信息
export function queryRooms(params, url = '/room.queryRooms') {
  params.communityId = getCommunityId()
  return new Promise((resolve, reject) => {
    request({
      url: url,
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询合同关联房间
export function queryContractRoom(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractRoom',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询组织树
export function listOrgTree(params) {
  params.communityId = getCommunityId()
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取审批人
export function getFirstStaff(params) {
  params.communityId = getCommunityId()
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow/getFirstStaff',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存合同变更计划
export function saveContractChangePlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/saveContractChangePlan',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}