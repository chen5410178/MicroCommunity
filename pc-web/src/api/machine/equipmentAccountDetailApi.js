import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取设备详情
export function getEquipmentAccountDetail(params) {
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

// 获取设备操作日志
export function getEquipmentLogs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentOperatingLog.listEquipmentOperatingLog',
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