import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取保养标准列表
export function listMaintainanceStandard(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.listMaintainanceStandard',
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

// 添加保养标准
export function saveMaintainanceStandard(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.saveMaintainanceStandard',
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

// 更新保养标准
export function updateMaintainanceStandard(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.updateMaintainanceStandard',
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

// 删除保养标准
export function deleteMaintainanceStandard(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainance.deleteMaintainanceStandard',
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