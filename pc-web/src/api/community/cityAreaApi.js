import request from '@/utils/request'

// 获取地区树
export function getAreaTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/area.queryAreaTree',
      method: 'get',
      params: { hc: 1.8 }
    }).then(response => {
      const res = response.data

        resolve(res)

    }).catch(error => {
      reject(error)
    })
  })
}

// 获取地区详情
export function getAreaDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/area.listAreas',
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

// 添加地区
export function addArea(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/area.saveCityArea',
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

// 更新地区
export function updateArea(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/area.updateCityArea',
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

// 删除地区
export function deleteArea(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/area.deleteCityArea',
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