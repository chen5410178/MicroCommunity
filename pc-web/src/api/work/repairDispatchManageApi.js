import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取报修单列表
export function listStaffRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listStaffRepairs',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报修单列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取报修类型
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报修类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取维修师傅列表
export function listRepairTypeUsers(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairTypeUsers',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取维修师傅列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 报修派单
export function repairDispatch(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.repairDispatch',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '报修派单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 回访工单
export function appraiseRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.appraiseRepair',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '回访工单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 暂停报修
export function repairStop(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.repairStop',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '暂停报修失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 启动报修
export function repairStart(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.repairStart',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '启动报修失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}