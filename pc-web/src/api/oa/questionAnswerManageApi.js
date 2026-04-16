import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取问卷列表
export function listQuestionAnswer(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listQuestionAnswer',
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

// 更新问卷信息
export function updateQuestionAnswer(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/questionAnswer.updateQuestionAnswer',
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

// 删除问卷
export function deleteQuestionAnswer(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.deleteQuestionAnswer',
      method: 'post',
      data: {
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

// 发布问卷
export function publishQuestionAnswer(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.publishQuestion',
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

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}