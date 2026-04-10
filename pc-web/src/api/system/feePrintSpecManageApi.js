import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询打印配置列表
export function queryFeePrintSpec(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      ...params
    }
    request({
      url: '/feePrintSpec/queryFeePrintSpec',
      method: 'get',
      params: defaultParams
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

// 添加打印配置
export function saveFeePrintSpec(data) {
  return new Promise((resolve, reject) => {
    const postData = {
      ...data,
      communityId: getCommunityId()
    }
    request({
      url: '/feePrintSpec/saveFeePrintSpec',
      method: 'post',
      data: postData
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Add failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新打印配置
export function updateFeePrintSpec(data) {
  return new Promise((resolve, reject) => {
    const postData = {
      ...data,
      communityId: getCommunityId()
    }
    request({
      url: '/feePrintSpec/updateFeePrintSpec',
      method: 'post',
      data: postData
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Update failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除打印配置
export function deleteFeePrintSpec(data) {
  return new Promise((resolve, reject) => {
    const postData = {
      ...data,
      communityId: getCommunityId()
    }
    request({
      url: '/feePrintSpec/deleteFeePrintSpec',
      method: 'post',
      data: postData
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Delete failed'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}