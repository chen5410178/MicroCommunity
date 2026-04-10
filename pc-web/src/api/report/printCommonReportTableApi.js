import request from '@/utils/request'

/**
 * 获取通用报表数据
 * @param {Object} params 查询参数
 * @returns {Promise} Promise对象
 */
export function getPrintCommonReportTableData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/report/api/printCommonReportTable.getPrintCommonReportTableData',
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

/**
 * 保存通用报表数据
 * @param {Object} data 报表数据
 * @returns {Promise} Promise对象
 */
export function savePrintCommonReportTableData(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/report/api/printCommonReportTable.savePrintCommonReportTableData',
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

/**
 * 打印通用报表
 * @param {Object} data 打印数据
 * @returns {Promise} Promise对象
 */
export function printCommonReportTable(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/report/api/printCommonReportTable.printCommonReportTable',
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