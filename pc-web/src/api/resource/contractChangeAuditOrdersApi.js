import request from '@/utils/request'

// 查询合同变更任务列表
export function queryContractChangeTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractChangeTask',
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

// 审批合同变更计划
export function needAuditContractPlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/needAuditContractPlan',
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

// 查询组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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

// 查询工作流下一节点
export function listWorkflowNextNode(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflowNextNode',
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