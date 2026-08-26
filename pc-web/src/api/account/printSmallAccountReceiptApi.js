import request from '@/utils/request'

// 获取账户收据列表
export function listAccountReceipt(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/receipt.listAccountReceipt',
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

// 查询费用打印规格
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

// 获取打印机列表
export function listMachinePrinter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listMachinePrinter',
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

// 打印账户收据
export function printAccountReceipt(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/print.printAccountReceipt',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}