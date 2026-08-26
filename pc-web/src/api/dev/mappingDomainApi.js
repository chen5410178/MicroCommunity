import request from '@/utils/request'

// 获取配置项列表
export function getMappingDomainList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.listMappingDomain',
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

// 添加配置项
export function addMappingDomain(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.saveMappingDomain',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新配置项
export function updateMappingDomain(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.updateMappingDomain',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除配置项
export function deleteMappingDomain(id) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.deleteMappingDomain',
      method: 'post',
      data: { id }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
