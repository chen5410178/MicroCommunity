import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取保养任务详情列表
export function getMaintainanceTaskDetailList(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/maintainanceTask.listMaintainanceTaskDetail',
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

// 获取保养计划列表
export function getMaintainancePlanList(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/maintainancePlan.listMaintainancePlan',
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

// 导出Excel
export function exportMaintainanceTaskDetail(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/callComponent/exportReportFee/exportData',
      method: 'get',
      params: {
        pagePath: 'maintainanceTaskDetails',
        ...params
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}