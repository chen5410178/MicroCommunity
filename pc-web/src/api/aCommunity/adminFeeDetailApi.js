import request from '@/utils/request'

// 获取费用详情列表
export function getFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listAdminFee',
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

// 更新费用信息
export function updateFeeDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.updateAdminFee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新费用信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除费用信息
export function deleteFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.deleteAdminFee',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除费用信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}