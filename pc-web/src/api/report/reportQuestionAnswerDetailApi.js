import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询问卷回答明细
export function queryUserQuestionAnswerValue(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportQuestionAnswer/queryUserQuestionAnswerValue',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出问卷回答明细
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}