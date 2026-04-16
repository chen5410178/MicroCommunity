import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取业主投票列表
export function listOwnerVote(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listOwnerVote',
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

// 获取投票详情
export function getOwnerVoteDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.getOwnerVoteDetail',
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

// 添加投票
export function addOwnerVote(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.addOwnerVote',
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

// 删除投票
export function deleteOwnerVote(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.deleteOwnerVote',
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
export function deleteQuestionAnswer(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.deleteQuestionAnswer',
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

// 发布投票
export function publishQuestion(data) {
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