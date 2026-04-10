import request from '@/utils/request'

// 查询OA工作流表单配置
export function queryOaWorkflowForm(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowForm',
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

// 查询OA工作流表单数据
export function queryOaWorkflowFormData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowFormData',
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

// 保存OA工作流表单数据
export function saveOaWorkflowFormData(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/saveOaWorkflowFormData',
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

// 查询OA工作流用户任务表单数据
export function queryOaWorkflowUserTaskFormData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowUserTaskFormData',
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

// 查询OA工作流用户历史任务表单数据
export function queryOaWorkflowUserHisTaskFormData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowUserHisTaskFormData',
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

// 上传文件
export function uploadVedio(data, config) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadVedio/upload',
      method: 'post',
      data,
      ...config
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
      url: '/workflow/listRunWorkflowImage',
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