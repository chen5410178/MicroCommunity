import request from '@/utils/request'

// 查询房屋欠费信息
export function getRoomOweFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportOweFee/queryReportOweFee',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取房屋欠费信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}