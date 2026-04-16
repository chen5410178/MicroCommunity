import request from '@/utils/request'

// 获取小区列表
export function listCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取地区列表
export function listAreas(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/area.listAreas',
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

// 保存小区
export function saveCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.saveCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新小区
export function updateCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.updateCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 删除小区
export function deleteCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.deleteCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 同步小区到IOT
export function sendCommunityToIot(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.sendCommunityToIot',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 撤回审核
export function auditEnterCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.auditEnterCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data

      resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}