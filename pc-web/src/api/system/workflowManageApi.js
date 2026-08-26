import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取流程列表
export function listWorkflows(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflows',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取流程图片
export function listWorkflowImage(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/workflow.listWorkflowImage',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code != 0) {
        reject(new Error(res.msg))
        return
      }
      resolve({
        data: res.data
      })
}).catch(error => {
      reject(error)
    })
  })
}