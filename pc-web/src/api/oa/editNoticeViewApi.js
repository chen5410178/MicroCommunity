import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取公告详情
export function getNoticeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notice.listNotices',
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

// 更新公告
export function updateNotice(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notice.updateNotice',
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
export function uploadNoticeImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: 'uploadImage',
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data'
      },
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