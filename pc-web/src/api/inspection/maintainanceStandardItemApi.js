import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取保养标准项列表
export function listMaintainanceStandardItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.listMaintainanceStandardItem',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取所有保养项列表
export function listMaintainanceItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.listMaintainanceItem',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存保养标准项
export function saveMaintainanceStandardItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.saveMaintainanceStandardItem',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除保养标准项
export function deleteMaintainanceStandardItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.deleteMaintainanceStandardItem',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}