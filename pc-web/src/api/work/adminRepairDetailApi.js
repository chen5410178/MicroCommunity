import request from '@/utils/request'

// 获取报修详情
export function getRepairDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listAdminOwnerRepairs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报修详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取维修人员列表
export function listRepairStaffs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listAdminRepairStaffs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取维修人员列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品使用记录
export function listStoreUseRecords(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAdminStoreUseRecords',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取物品使用记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 回复评价
export function replyRepairAppraise(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.replyRepairAppraise',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '回复评价失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}