import request from '@/utils/request'

/**
 * 查询费用汇总数据
 * @param {Object} params 查询参数
 */
export function queryReportFeeSummary(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportFeeSummary',
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
 * 查询楼栋费用汇总数据
 * @param {Object} params 查询参数
 */
export function queryReportFloorFeeSummary(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportFloorFeeSummary',
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
 * 查询费用项汇总数据
 * @param {Object} params 查询参数
 */
export function queryReportConfigFeeSummary(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportConfigFeeSummary',
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
 * 导出数据
 * @param {Object} params 导出参数
 */
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
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
 * 查询楼栋列表
 * @param {Object} params 查询参数
 */
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

/**
 * 查询费用配置列表
 * @param {Object} params 查询参数
 */
export function queryFeeConfigs(params) {
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

/**
 * 查询用户关联的小区列表
 */
export function queryCommunitys() {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listMyEnteredCommunitys',
      method: 'get',
      params: {
        _uid: '123mlkdinkldldijdhuudjdjkkd',
        page: 1,
        row: 100
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}