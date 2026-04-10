import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取员工列表
export function listStaffs(params) {
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

// 获取员工关联的小区列表
export function listStaffCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.listStaffCommunity',
      method: 'get',
      params: {
        ...params,
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取可关联的小区列表
export function listWaitStaffCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.listWaitStaffCommunity',
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

// 获取组织树
export function listOrgTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: {
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 关联小区到员工
export function saveStaffCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.saveStaffCommunity',
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

// 删除员工小区关联
export function deleteStaffCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/role.deleteStaffCommunity',
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