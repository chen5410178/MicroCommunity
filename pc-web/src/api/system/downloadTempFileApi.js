import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取下载文件列表
export function listUserDownloadFile(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/userDownloadFile.listUserDownloadFile',
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

// 删除下载文件
export function deleteUserDownloadFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/userDownloadFile.deleteUserDownloadFile',
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