import request from '@/utils/request'

// 查询欠费列表
export function listOweFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeApi/listOweFees',
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

// 查询房间信息
export function listRoomsWhereFeeSet(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listRoomsWhereFeeSet',
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

// 查询打印规范
export function queryFeePrintSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feePrintSpec/queryFeePrintSpec',
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