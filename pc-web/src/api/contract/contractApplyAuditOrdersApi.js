import request from '@/utils/request'

// 查询合同任务列表
export function queryContractTask(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractTask',
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

// 合同审批
export function needAuditContract(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/needAuditContract',
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

// 查询工作流步骤人员
export function listWorkflowStepStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflowStepStaffs',
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