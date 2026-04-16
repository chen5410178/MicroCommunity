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
      resolve(res)
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
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 终止合同
export function stopContract(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/stopContract',
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