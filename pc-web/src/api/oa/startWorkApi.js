import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询发起工作单列表
export function queryStartWork(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.queryStartWork',
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

// 删除工作单
export function deleteWorkPool(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.deleteWorkPool',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 启动/停止工作单
export function updateWorkState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/work.startOrStopWorkPool',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg))
      }
    }).catch(error => {
      reject(error)
    })
  })
}