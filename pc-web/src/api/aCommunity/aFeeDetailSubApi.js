import request from '@/utils/request'

// 获取子费用列表
export function listAdminPayFeeSub(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeSub.listAdminPayFeeSub',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取子费用列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取费用信息
export function listAdminFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listAdminFee',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取费用信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}