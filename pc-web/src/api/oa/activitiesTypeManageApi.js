import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询活动类型列表
export function queryActivitiesType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activitiesType/queryActivitiesType',
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

// 添加活动类型
export function saveActivitiesType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activitiesType/saveActivitiesType',
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

// 更新活动类型
export function updateActivitiesType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activitiesType/updateActivitiesType',
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

// 删除活动类型
export function deleteActivitiesType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/activitiesType/deleteActivitiesType',
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