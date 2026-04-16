import request from '@/utils/request'

// 查询小区报修树形数据
export function queryCommunityRepairTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityRepairTree',
      method: 'get',
      params: { hc: 1.8 }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取小区报修树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询管理员报修列表
export function listAdminOwnerRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listAdminOwnerRepairs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取报修列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}