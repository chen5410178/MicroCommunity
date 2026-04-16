import request from '@/utils/request'

// 获取配置中心列表
export function getMappingList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.listMappings',
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

// 添加配置中心
export function addMapping(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.saveMapping',
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

// 更新配置中心
export function updateMapping(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.updateMapping',
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

// 删除配置中心
export function deleteMapping(id) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mapping.deleteMapping',
      method: 'post',
      data: { id }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}
