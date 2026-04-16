import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询楼栋和单元信息
export function queryFloorAndUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloorAndUnits',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房屋信息
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询题目列表
export function listQuestionTitle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listQuestionTitle',
      method: 'get',
      params: {
        ...params,
        communityId: params.communityId || getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存问卷
export function saveQuestionAnswer(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.saveQuestionAnswer',
      method: 'post',
      data: {
        ...data,
        communityId: data.communityId || getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}