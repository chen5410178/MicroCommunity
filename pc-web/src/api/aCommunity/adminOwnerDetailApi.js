import request from '@/utils/request'

// 获取业主详情
export function getOwnerDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryAdminOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新业主信息
export function updateOwnerInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.updateOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新业主信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取业主属性列表
export function getOwnerAttributes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.listOwnerAttributes',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主属性列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}