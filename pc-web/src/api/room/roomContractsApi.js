import request from '@/utils/request'

// 查询房屋合同信息
export function getRoomContracts(params, roomId) {
  return new Promise((resolve, reject) => {
    const url = roomId ? '/contract.queryContractByRoomId' : '/contract/queryContract'
    
    request({
      url,
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取房屋合同信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}