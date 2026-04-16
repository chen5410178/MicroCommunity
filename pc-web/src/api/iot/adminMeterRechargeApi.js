import request from '@/utils/request'

// 获取充值明细列表
export function listAdminMeterCharge(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.listAdminMeterCharge',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取充值明细列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取管理员小区列表
export function listAdminCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listAdminCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取管理员小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}