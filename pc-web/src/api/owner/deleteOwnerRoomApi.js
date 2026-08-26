import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 删除业主房屋
export function deleteOwnerRoom(data) {
  return new Promise((resolve, reject) => {
    // 确保包含社区ID
    if (!data.communityId) {
      data.communityId = getCommunityId()
    }
    
    request({
      url: '/owner.deleteOwnerRoom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除业主房屋失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}