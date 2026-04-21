import request from '@/utils/request'

// 查询合同详情
export function queryContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询合同详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取合同房屋列表
export function getContractRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractRoom',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取合同房屋列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取合同附件列表
export function getContractFiles(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/getContractFiles',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取合同附件列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取合同费用列表
export function getContractFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/getContractFees',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取合同费用列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取合同历史缴费记录
export function getContractHisFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/getContractHisFees',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取合同历史缴费记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}