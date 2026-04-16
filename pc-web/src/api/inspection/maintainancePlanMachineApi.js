import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取保养计划设备列表
export function listMaintainancePlanMachine(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.listMaintainancePlanMachine',
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

// 获取设备列表
export function listEquipmentAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.listEquipmentAccount',
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

// 保存保养计划设备
export function saveMaintainancePlanMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.saveMaintainancePlanMachine',
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

// 删除保养计划设备
export function deleteMaintainancePlanMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.deleteMaintainancePlanMachine',
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