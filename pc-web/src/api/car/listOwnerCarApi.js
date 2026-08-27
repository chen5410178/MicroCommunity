import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询业主车辆
export function queryOwnerCars(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/owner.queryOwnerCars',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主车辆
export function deleteOwnerCars(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/owner.deleteOwnerCars',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导入车辆数据
export function importData(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    const formData = new FormData()
    formData.append('uploadFile', data.file)
    formData.append('communityId', communityId)
    formData.append('importAdapt', data.importAdapt || 'importOwnerCar')
    
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error.response.data)
    })
  })
}

// 修改车辆信息
export function editOwnerCar(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/owner.editOwnerCar',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出车辆数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/export.exportData',
      method: 'get',
      params: {
        ...params,
        communityId,
        pagePath: 'exportOwnerCar'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '导出失败'))
      }  
      }).catch(error => {
      reject(error)
    })
  })
}