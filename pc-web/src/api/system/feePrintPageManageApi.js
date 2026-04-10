import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取收据模板列表
export function listFeePrintPage(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPage.listFeePrintPage',
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

// 获取收据页面模板列表
export function listFeePrintPageTemplate(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPageTemplate.listFeePrintPageTemplate',
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

// 添加收据模板
export function saveFeePrintPage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPage.saveFeePrintPage',
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

// 更新收据模板
export function updateFeePrintPage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPage.updateFeePrintPage',
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

// 删除收据模板
export function deleteFeePrintPage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPage.deleteFeePrintPage',
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

// 更新收据模板状态
export function updateFeePrintPageState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintPage.updateFeePrintPage',
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