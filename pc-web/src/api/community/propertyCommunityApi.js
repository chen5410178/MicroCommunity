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
      if (res.code == 0) {
        resolve({
          data: res.communitys,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取未加入的小区列表
export function listNoEnterCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/storeEnterCommunity/listNoEnterCommunity',
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

// 获取菜单组列表
export function listMenuGroup(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroup.listMenuGroup',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '获取菜单组列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取小区菜单组列表
export function listMenuGroupCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroupCommunity.listMenuGroupCommunity',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '获取小区菜单组列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 加入小区
export function joinCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/member.join.community',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '加入小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新小区菜单组
export function updateMenuGroupCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menuGroupCommunity.updateMenuGroupCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新小区菜单组失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 退出小区
export function quitCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/member.quit.community',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '退出小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}