import request from '@/utils/request'

// 获取项目列表
export function getHcApiList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApi.listHcApi',
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
export function addHcApi(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApi.saveHcApi',
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
export function updateHcApi(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApi.updateHcApi',
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
export function deleteHcApi(apiId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/hcApi.deleteHcApi',
      method: 'post',
      data: { apiId }
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