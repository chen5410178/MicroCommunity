import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取抄表类型列表
export function listMeterType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterType.listMeterType',
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

// 添加抄表类型
export function saveMeterType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterType.saveMeterType',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新抄表类型
export function updateMeterType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterType.updateMeterType',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除抄表类型
export function deleteMeterType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterType.deleteMeterType',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}