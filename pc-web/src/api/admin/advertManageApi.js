import request from '@/utils/request'

// 获取广告列表
export function listAdverts(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/advert.listAdverts',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
        resolve({
          data: res.adverts,
          total: res.total
        })
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加广告
export function saveAdvert(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/advert.saveAdvert',
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

// 更新广告
export function updateAdvert(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/advert.updateAdvert',
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

// 删除广告
export function deleteAdvert(advertId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/advert.deleteAdvert',
      method: 'post',
      data: { advertId }
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

// 上传文件
export function uploadFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}
// 获取广告详情
export function getAdvertDetail(advertId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/advert.getAdvertDetail',
      method: 'get',
      params: { advertId }
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取广告项列表
export function getAdvertItems(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/advert.listAdvertItems',
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

// 获取位置类型
export function getLocationTypes() {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.listDict',
      method: 'get',
      params: { type: 'location_type_cd' }
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}