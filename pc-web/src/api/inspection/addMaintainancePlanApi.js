import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存保养计划
export function saveMaintainancePlan(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/maintainancePlan.saveMaintainancePlan',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

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
      resolve(response.data)
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
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}