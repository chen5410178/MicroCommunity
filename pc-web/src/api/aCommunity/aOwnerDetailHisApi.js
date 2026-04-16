import request from '@/utils/request'

export function queryAdminHisOwner(params) {
  return new Promise((resolve, reject) => {
    return request({
      url: '/owner.queryAdminHisOwner',
      method: 'get',
      params
    }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}

export function getOwnerAttrSpec() {
  return request({
    url: '/attrSpec.queryAttrSpec',
    method: 'get',
    params: {
      specCd: 'building_owner_attr'
    }
  }).then(res => {
    return res.data
  })
}