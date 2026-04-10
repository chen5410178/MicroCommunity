import request from '@/utils/request'

// 查询工单表单
export function queryOaWorkflowForm(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowForm',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询工单表单数据
export function queryOaWorkflowFormData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowFormData',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询工单用户
export function queryOaWorkflowUser(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/queryOaWorkflowUser',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取下一个任务
export function getNextTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/getNextTask',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 审核工单
export function auditOaWorkflow(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/oaWorkflow/auditOaWorkflow',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
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
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}