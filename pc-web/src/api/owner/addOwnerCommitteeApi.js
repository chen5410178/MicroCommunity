import request from '@/utils/request'

// 保存业委会信息
export function saveOwnerCommittee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerCommittee.saveOwnerCommittee',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存业委会信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}