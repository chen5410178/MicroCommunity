import request from '@/utils/request'

// 获取小区商铺列表
export function getShopCommunityList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getAdminMallOpenApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0 || res.code === 200) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get shop community list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}