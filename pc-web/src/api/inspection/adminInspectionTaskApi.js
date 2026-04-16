import request from '@/utils/request'

// 查询巡检任务列表
export function listAdminInspectionTasks(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionTask.listAdminInspectionTasks',
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

// 查询社区巡检树
export function queryCommunityInspectionTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityInspectionTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取社区巡检树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}