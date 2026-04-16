import request from '@/utils/request'

// 查询合同列表
export function getContractList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
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

// 查询合同类型列表
export function getContractTypeList(params) {
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

// 查询合同规格
export function getContractTypeSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractTypeSpec',
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

// 更新合同信息
export function updateContract(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/updateContract',
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

// 删除合同
export function deleteContract(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/deleteContract',
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

// 上传合同文件
export function uploadContractFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractFile/uploadContactFile',
      method: 'post',
      data,
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