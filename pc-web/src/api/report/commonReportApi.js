import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取报表自定义列表
export function listReportCustom(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustom.listReportCustom',
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

// 获取报表自定义组件关系
export function listReportCustomComponentRel(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentRel.listReportCustomComponentRel',
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

// 获取报表自定义组件条件
export function listReportCustomComponentCondition(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentCondition.listReportCustomComponentCondition',
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

// 获取报表自定义组件数据
export function listReportCustomComponentData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponent.listReportCustomComponentData',
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

// 获取报表自定义组件页脚数据
export function listReportCustomComponentDataFooter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponent.listReportCustomComponentDataFooter',
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

// 导出报表数据
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