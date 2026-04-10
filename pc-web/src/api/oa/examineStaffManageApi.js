import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取考核人员列表
export function listExamineStaff(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.listExamineStaff',
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

// 删除考核人员
export function deleteExamineStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/examine.deleteExamineStaff',
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