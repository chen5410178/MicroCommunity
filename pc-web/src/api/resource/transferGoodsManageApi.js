import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取用户可转赠的物品列表
export function listUserStorehouses(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.listUserStorehouses',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品类型列表
export function listResourceStoreTypes(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params: {
        ...params,
        communityId
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
export function listOrgTree() {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: {
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
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
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存转赠信息
export function saveAllocationUserStorehouse(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.saveAllocationUserStorehouse',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

export default {
  listUserStorehouses,
  listResourceStoreTypes,
  listOrgTree,
  queryStaffInfos,
  saveAllocationUserStorehouse
}