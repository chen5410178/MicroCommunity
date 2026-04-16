import request from '@/utils/request'

// 查询房间列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
      method: 'get',
      params
    })
      .then(response => {
        const res = response.data
          resolve(res)
      })
      .catch(error => {
        reject(error)
      })
  })
}

// 导出费用导入模板
export function exportFeeImportExcel(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params
    })
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || '导出模板失败'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

// 导入费用数据
export function importCreateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.importCreateFee',
      method: 'post',
      data
    })
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || '导入费用失败'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

// 批量创建费用
export function batchCreateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.batchCreateFee',
      method: 'post',
      data
    })
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || '批量创建费用失败'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

// 下载催缴单
export function downloadCollectionLetter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.downloadCollectionLetter',
      method: 'get',
      params
    })
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || '下载催缴单失败'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}