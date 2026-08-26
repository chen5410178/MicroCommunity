import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取巡检计划列表
export function listInspectionPlans() {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionPlan.listInspectionPlans',
      method: 'get',
      params: {
        page: 1,
        row: 1000,
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

// 获取巡检任务列表
export function listInspectionTasks(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionTask.listInspectionTasks',
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

// 获取巡检任务详情
export function listInspectionTaskDetails(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionTaskDetail.listInspectionTaskDetails',
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

// 更新巡检任务（流转）
export function updateInspectionTask(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionTask.updateInspectionTask',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新巡检任务失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除巡检任务
export function deleteInspectionTask(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/inspectionTask.deleteInspectionTask',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除巡检任务失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

