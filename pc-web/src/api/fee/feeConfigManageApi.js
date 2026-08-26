import request from '@/utils/request'

// 查询费用类型树
export function queryFeeTypeTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeType.queryFeeTypeTree',
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

// 查询费用类型列表
export function listFeeType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeType.listFeeType',
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

// 添加费用类型
export function saveFeeType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeType.saveFeeType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加费用类型
export function updateFeeType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeType.updateFeeType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除费用类型
export function deleteFeeType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeType.deleteFeeType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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

// 添加费用配置
export function saveFeeConfig(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.saveFeeConfig',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改费用配置
export function updateFeeConfig(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.updateFeeConfig',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除费用配置
export function deleteFeeConfig(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.deleteFeeConfig',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}