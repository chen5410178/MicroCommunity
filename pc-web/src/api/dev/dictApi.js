import request from '@/utils/request'

// 获取字典列表
export function getDictList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.listDict',
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

// 添加字典
export function addDict(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.saveDict',
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

// 更新字典
export function updateDict(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.updateDict',
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

// 删除字典
export function deleteDict(id) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.deleteDict',
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


// 获取字典规格列表
export function getDictSpecList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dictSpec.listDictSpec',
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
