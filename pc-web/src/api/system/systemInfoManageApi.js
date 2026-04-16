import request from '@/utils/request'

// 获取系统配置列表
export function listSystemInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/system.listSystemInfo',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取系统配置列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新系统配置
export function updateSystemInfo(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/system.updateSystemInfo',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新系统配置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function getPropertyUrl() {
  let systemInfo = localStorage.getItem('java110SystemInfo')
  if (systemInfo) {
    systemInfo = JSON.parse(systemInfo)
    return systemInfo.propertyUrl
  }
  return ''
}