import request from '@/utils/request'

// 获取项目列表
export function getApiTypeList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/apiType.listApiType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(response)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addApiType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/apiType.saveApiType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新项目
export function updateApiType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/apiType.updateApiType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除项目
export function deleteApiType(typeId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/apiType.deleteApiType',
      method: 'post',
      data: { typeId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 