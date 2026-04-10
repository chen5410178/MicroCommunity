import request from '@/utils/request'

// 查询业主报修列表
export function listOwnerRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主报修列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加业主报修
export function saveOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.saveOwnerRepair',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加业主报修失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新业主报修
export function updateOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.updateOwnerRepair',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新业主报修失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主报修
export function deleteOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.deleteOwnerRepair',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除业主报修失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询报修类型列表
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报修类型列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询楼栋列表
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取楼栋列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询单元列表
export function queryUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/unit.queryUnits',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取单元列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询房屋列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取房屋列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}