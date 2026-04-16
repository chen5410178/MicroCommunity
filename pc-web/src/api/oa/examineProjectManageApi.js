import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取考核项目列表
export function listExamineProject(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.listExamineProject',
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

// 添加考核项目
export function saveExamineProject(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.saveExamineProject',
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

// 更新考核项目
export function updateExamineProject(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.updateExamineProject',
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

// 删除考核项目
export function deleteExamineProject(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.deleteExamineProject',
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