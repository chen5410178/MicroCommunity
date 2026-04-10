import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询场馆列表
export function listCommunityVenue(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/communityVenue.listCommunityVenue',
      method: 'get',
      params
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 添加场馆
export function saveCommunityVenue(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/communityVenue.saveCommunityVenue',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加场馆失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改场馆
export function updateCommunityVenue(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/communityVenue.updateCommunityVenue',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '修改场馆失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除场馆
export function deleteCommunityVenue(venueId) {
  return new Promise((resolve, reject) => {
    const data = {
      venueId,
      communityId: getCommunityId()
    }
    request({
      url: '/communityVenue.deleteCommunityVenue',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除场馆失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询场地列表
export function listCommunitySpace(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/communitySpace.listCommunitySpace',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取场地列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加场地
export function saveCommunitySpace(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/communitySpace.saveCommunitySpace',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加场地失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改场地
export function updateCommunitySpace(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/communitySpace.updateCommunitySpace',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '修改场地失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除场地
export function deleteCommunitySpace(spaceId) {
  return new Promise((resolve, reject) => {
    const data = {
      spaceId,
      communityId: getCommunityId()
    }
    request({
      url: '/communitySpace.deleteCommunitySpace',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除场地失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新开放时间
export function updateCommunitySpaceOpenTime(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/communitySpaceOpenTime.updateCommunitySpaceOpenTime',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新开放时间失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}