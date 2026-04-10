import request from '@/utils/request'

// 获取属性列表
export function getAttrSpecList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrSpec/queryAttrSpec',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取属性列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加属性
export function addAttrSpec(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrSpec/saveAttrSpec',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加属性失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新属性
export function updateAttrSpec(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrSpec/updateAttrSpec',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新属性失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除属性
export function deleteAttrSpec(specId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrSpec/deleteAttrSpec',
      method: 'post',
      data: { specId }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除属性失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
