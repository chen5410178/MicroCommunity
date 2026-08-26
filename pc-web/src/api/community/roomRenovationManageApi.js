import request from '@/utils/request'

// 查询装修信息列表
export function listRoomRenovation(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/queryRoomRenovation',
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

// 添加装修信息
export function addRoomRenovation(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/saveRoomRenovation',
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

// 更新装修信息
export function updateRoomRenovation(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/updateRoomRenovation',
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

// 删除装修信息
export function deleteRoomRenovation(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/deleteRoomRenovation',
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

// 更新装修状态
export function updateRoomRenovationState(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/updateRoomRenovationState',
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

// 更新装修审核状态
export function updateRoomToExamine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/updateRoomToExamine',
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

// 保存装修验收信息
export function saveRoomRenovationDetail(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/roomRenovation/saveRoomRenovationDetail',
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