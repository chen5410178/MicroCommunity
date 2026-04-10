import request from '@/utils/request'

// 查询合同类型列表
export function queryContractType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractType',
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

// 添加合同类型
export function saveContractType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/saveContractType',
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

// 修改合同类型
export function updateContractType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/updateContractType',
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

// 删除合同类型
export function deleteContractType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/deleteContractType',
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

// 查询合同类型模板
export function queryContractTypeTemplate(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractTypeTemplate',
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

// 保存合同类型模板
export function saveContractTypeTemplate(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/saveContractTypeTemplate',
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

// 更新合同类型模板
export function updateContractTypeTemplate(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/updateContractTypeTemplate',
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

// 打印合同模板
export function printContractTemplate(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/printContractTemplate',
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