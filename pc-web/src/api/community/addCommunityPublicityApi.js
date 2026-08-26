import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存社区公示
export function saveCommunityPublicity(data) {
  data.communityId = getCommunityId()
  return new Promise((resolve, reject) => {
    request({
      url: '/publicity.saveCommunityPublicity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  data.communityId = getCommunityId()
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile/uploadImage',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}