import request from '@/utils/request'

// 查询费用列表
export function listFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listFee',
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

// 删除费用
export function deleteFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.deleteFee',
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

// 更新费用
export function updateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.updateFee',
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

// 拆分费用
export function splitFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeSub.splitPayFee',
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

// 保存车位费用
export function saveParkingSpaceCreateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.saveParkingSpaceCreateFee',
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

// 保存抄表
export function saveMeterWater(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterWater.saveMeterWater',
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

// 查询抄表
export function queryPreMeterWater(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterWater/queryPreMeterWater',
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

// 查询费用配置
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
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

// 查询停车场
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingArea.listParkingAreas',
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

// 查询楼栋
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
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

// 查询单元
export function queryUnits(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/unit.queryUnits',
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

// 查询房屋
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

// 查询抄表类型
export function listMeterType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/meterType.listMeterType',
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