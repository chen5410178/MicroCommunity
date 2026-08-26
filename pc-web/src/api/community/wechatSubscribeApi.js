import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取微信关注用户列表
export function listWechatSubscribe(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/wechat.listWechatSubscribe',
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

// 同步微信关注用户
export function synchronizeWechatSubscribe(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/wechat.synchronizeWechatSubscribe',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
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