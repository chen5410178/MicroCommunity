import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 物品领用提交
export function goodsCollection(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/collection/goodsCollection',
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

// 获取仓库列表
export function listStorehouses(params = {}) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      page: 1,
      row: 100,
      communityId: getCommunityId(),
      allowUse: 'ON'
    }
    
    request({
      url: '/resourceStore.listStorehouses',
      method: 'get',
      params: {...defaultParams, ...params}
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品类型列表
export function listResourceStoreTypes(params = {}) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      page: 1,
      row: 100,
      communityId: getCommunityId(),
      parentId: params.parentId || '0'
    }
    
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params: {...defaultParams, ...params}
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品列表
export function listResourceStores(params = {}) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      page: 1,
      row: 10,
      communityId: getCommunityId(),
      ...params
    }
    
    request({
      url: '/resourceStore.listResourceStores',
      method: 'get',
      params: defaultParams
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取组织树
export function listOrgTree(params = {}) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取员工信息列表
export function listStaffInfos(params = {}) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      page: 1,
      rows: 50,
      ...params
    }
    
    request({
      url: '/query.staff.infos',
      method: 'get',
      params: defaultParams
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取流程审批人
export function queryFirstAuditStaff(params = {}) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow.queryFirstAuditStaff',
      method: 'get',
      params: {
        communityId: getCommunityId(),
        ...params
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}