import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取调拨记录列表
export function listAllocationStorehouseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouseApplys',
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

// 获取仓库列表
export function listStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listStorehouses',
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

// 保存调拨申请
export function saveAllocationStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.saveAllocationStorehouse',
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

// 删除调拨记录
export function deleteAllocationStorehouse(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.deleteAllocationStorehouse',
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

// 获取流程图
export function listRunWorkflowImage(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listRunWorkflowImage',
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

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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