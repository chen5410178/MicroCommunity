import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取工单详情
export function getRepairDetail(repairId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params: {
        page: 1,
        row: 1,
        communityId,
        repairId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data[0])
      } else {
        reject(new Error(res.msg || '获取工单详情失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工单图片
export function getRepairPhotos(repairId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params: {
        page: 1,
        row: 1,
        communityId,
        repairId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          photos: res.data[0].repairPhotos || [],
          beforePhotos: res.data[0].beforePhotos || [],
          afterPhotos: res.data[0].afterPhotos || []
        })
      } else {
        reject(new Error(res.msg || '获取工单图片失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取维修物品
export function getRepairResources(repairId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/resourceStore.listResourceStoreUseRecords',
      method: 'get',
      params: {
        page: 1,
        row: 100,
        communityId,
        repairId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        const resources = res.data.map(item => {
          if (item.resId === '666666') {
            return { ...item, rstName: '自定义', specName: '自定义' }
          }
          return item
        })
        resolve(resources)
      } else {
        reject(new Error(res.msg || '获取维修物品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工单流转
export function getRepairFlow(repairId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/ownerRepair.listRepairStaffs',
      method: 'get',
      params: {
        page: 1,
        row: 100,
        communityId,
        repairId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取工单流转失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取工单事件
export function getRepairEvents(repairId) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/repair.listRepairEvent',
      method: 'get',
      params: {
        page: 1,
        row: 100,
        communityId,
        repairId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取工单事件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 回复评价
export function replyAppraise(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/repair.replyRepairAppraise',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve()
      } else {
        reject(new Error(res.msg || '回复评价失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}