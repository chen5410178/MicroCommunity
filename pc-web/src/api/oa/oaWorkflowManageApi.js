import request from '@/utils/request'

// 查询流程列表
export function getOaWorkflowList(params) {
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

// 新增流程
export function saveOaWorkflow(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/saveOaWorkflow',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改流程
export function updateOaWorkflow(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/updateOaWorkflow',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除流程
export function deleteOaWorkflow(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/deleteOaWorkflow',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 发布流程
export function deployModel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow/deployModel',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}