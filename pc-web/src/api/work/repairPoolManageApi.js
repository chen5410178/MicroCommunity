import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询报修工单池
export function listRepairPools(params) {
  return new Promise((resolve, reject) => {
    const baseParams = {
      communityId: getCommunityId(),
      page: params.page || 1,
      row: params.row || 10
    }
    
    const queryParams = { ...baseParams, ...params }
    
    if (queryParams.state === 'TIMEOUT') {
      queryParams.state = ''
      queryParams.timeoutFlag = 'Y'
    }
    
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params: queryParams
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to fetch repair pools'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 派单
export function dispatchRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/ownerRepair.repairDispatch',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg ))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 结束工单
export function endRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.repairEnd',
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
        reject(new Error(res.msg || 'End repair failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新业主报修
export function updateOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.updateOwnerRepair',
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
        reject(new Error(res.msg || 'Update failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主报修
export function deleteOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.deleteOwnerRepair',
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
        reject(new Error(res.msg || 'Delete failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 抢单
export function grabbingRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.grabbingRepair',
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
        reject(new Error(res.msg || 'Grab order failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}