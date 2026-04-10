import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取业主投票信息
export function listOwnerVote(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      ...params
    }
    request({
      url: '/question.listOwnerVote',
      method: 'get',
      params: defaultParams
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取用户问题答案列表
export function listUserQuestionAnswer(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      ...params
    }
    request({
      url: '/question.listUserQuestionAnswer',
      method: 'get',
      params: defaultParams
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}