import request from '@/utils/request'

// 获取待办信息
export function getUndoInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/undo/list',
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

// 查询OA工作流列表
export function listNewOaWorkflows(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflow',
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