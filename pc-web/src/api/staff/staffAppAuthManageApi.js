import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询员工应用认证信息
export function queryStaffAppAuth(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/staff/queryStaffAppAuth',
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

// 生成认证二维码
export function generatorQrCode(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/staff/generatorQrCode',
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