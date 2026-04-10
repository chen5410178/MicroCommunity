import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取物品领用列表
export function listPurchaseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApply.listPurchaseApplys',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.purchaseApplys,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除物品领用申请
export function deletePurchaseApply(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/purchaseApply.deletePurchaseApply',
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

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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

// 获取工作流图片
export function listRunWorkflowImage(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listRunWorkflowImage',
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