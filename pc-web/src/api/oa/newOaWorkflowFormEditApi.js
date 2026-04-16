import request from '@/utils/request'

/**
 * 查询OA工作流表单
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
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

/**
 * 查询OA工作流表单数据
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
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

/**
 * 更新OA工作流表单数据
 * @param {Object} data 表单数据
 * @returns {Promise} Promise对象
 */
export function updateOaWorkflowFormData(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow.updateOaWorkflowFormData',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 上传视频文件
 * @param {FormData} formData 表单数据
 * @param {Object} config 配置项
 * @returns {Promise} Promise对象
 */
export function uploadVedio(formData, config) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadVedio/upload',
      method: 'post',
      data: formData,
      onUploadProgress: config.onUploadProgress,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}