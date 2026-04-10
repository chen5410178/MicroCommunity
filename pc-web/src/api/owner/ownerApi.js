import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询业主列表
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/owner.queryOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询业主列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存业主
export function saveOwner(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/owner.saveOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存业主失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 编辑业主
export function editOwner(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/owner.editOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '编辑业主失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主
export function deleteOwner(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/owner.deleteOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除业主失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    const formData = new FormData()
    formData.append('uploadFile', data.file)
    formData.append('communityId', getCommunityId())
    
    request({
      url: '/callComponent/upload/uploadFile/uploadImage',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取属性规格
export function getAttrSpec(specCd) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrSpec.listAttrSpecs',
      method: 'get',
      params: { specCd, communityId: getCommunityId() }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取属性规格失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取属性值
export function getAttrValue(specCd) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attrValue/queryAttrValue',
      method: 'get',
      params: { specCd, communityId: getCommunityId(), page: 1, row: 100 }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取属性值失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}