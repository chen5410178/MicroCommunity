import request from '@/utils/request'

// 获取Databus列表
export function getBusinessDatabusList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessDatabus/queryBusinessDatabus',
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

// 添加Databus
export function addBusinessDatabus(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessDatabus/saveBusinessDatabus',
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

// 更新Databus
export function updateBusinessDatabus(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessDatabus/updateBusinessDatabus',
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

// 删除Databus
export function deleteBusinessDatabus(databusId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessDatabus/deleteBusinessDatabus',
      method: 'post',
      data: { databusId }
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
