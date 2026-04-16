import request from '@/utils/request'

// 查询合同列表
export function queryContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询合同类型
export function queryContractType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询合同类型规格
export function queryContractTypeSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractTypeSpec',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询合同文件
export function queryContractFile(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contractFile/queryContractFile',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新合同
export function updateContract(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/updateContract',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
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
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传合同文件
export function uploadContactFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadContactFile',
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