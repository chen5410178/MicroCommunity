import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存公告信息
export function saveNotice(data) {
  return new Promise((resolve, reject) => {
    // 确保communityId存在
    if (!data.communityId) {
      data.communityId = getCommunityId()
    }
    
    request({
      url: '/notice.saveNotice',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('uploadFile', file)
  formData.append('communityId', getCommunityId())

  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
      method: 'post',
      data: formData,
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