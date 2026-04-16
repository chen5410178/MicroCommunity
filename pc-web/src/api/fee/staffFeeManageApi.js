import request from '@/utils/request'

// 获取员工收费列表
export function getStaffFeeList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/api.getStaffFee',
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

// 导出员工收费数据
export function exportStaffFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/exportReportFee/exportData',
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