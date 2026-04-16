import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取机器翻译列表
export function listMachineTranslates(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineTranslate.listMachineTranslates',
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

// 保存机器翻译
export function saveMachineTranslate(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineTranslate.saveMachineTranslate',
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

// 重新发送物联网同步
export function resendIot(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machine/resendIot',
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