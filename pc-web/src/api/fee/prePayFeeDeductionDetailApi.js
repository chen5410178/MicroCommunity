import request from '@/utils/request'

// 获取预付扣款详情列表
export function listPrePayFeeDetails(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.listPrePayFeeDetail',
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

// 添加预付扣款详情
export function addPrePayFeeDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.savePrePayFeeDetail',
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

// 更新预付扣款详情
export function updatePrePayFeeDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.updatePrePayFeeDetail',
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

// 删除预付扣款详情
export function deletePrePayFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/prePayFee.deletePrePayFeeDetail',
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