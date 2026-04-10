import request from '@/utils/request'

// 获取物业公司列表
export function getStoreList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.listProperty',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取物业公司列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}


// 添加物业公司
export function addStore(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.saveProperty',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加物业公司失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新物业公司
export function updateStore(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.updateProperty',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新物业公司失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除物业公司
export function deleteStore(storeId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/property.deleteProperty',
      method: 'post',
      data: { storeId }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除物业公司失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
