import request from '@/utils/request'

// 查询问卷信息
export function listQuestionAnswer(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listQuestionAnswer',
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

// 更新问卷信息
export function updateQuestionAnswer(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.updateQuestionAnswer',
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

// 查询题目列表
export function listQuestionTitle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/question.listQuestionTitle',
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

// 查询楼栋和单元信息
export function queryFloorAndUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloorAndUnits',
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

// 查询房屋信息
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
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