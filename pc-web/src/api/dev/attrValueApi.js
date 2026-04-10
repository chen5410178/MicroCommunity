import request from '@/utils/request'

// 获取属性值列表
export function getAttrValueList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrValue/queryAttrValue',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取属性值列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加属性值
export function addAttrValue(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrValue/saveAttrValue',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加属性值失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新属性值
export function updateAttrValue(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrValue/updateAttrValue',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新属性值失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除属性值
export function deleteAttrValue(valueId,specId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrValue/deleteAttrValue',
      method: 'post',
      data: { valueId,specId,communityId:'-1' }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除属性值失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
