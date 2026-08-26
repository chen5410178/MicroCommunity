import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询车辆收费信息
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

// 保存停车位收费信息
export function saveParkingSpaceCreateFee(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/fee.saveParkingSpaceCreateFee',
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

// 查询费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/feeConfig.listFeeConfigs',
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

// 查询停车场列表
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingArea.listParkingAreas',
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

// 导出车辆收费Excel
export function exportCarFeeExcel(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/callComponent/importAndExportFee/exportData',
      method: 'get',
      params: {
        ...params,
        communityId,
        type: '2002'
      }
    }).then(response => {
      resolve(response)
    }).catch(error => {
      reject(error)
    })
  })
}

// 导入自定义费用
export function importCustomFee(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    const formData = new FormData()
    formData.append('uploadFile', data.file)
    formData.append('communityId', communityId)
    formData.append('importAdapt', 'importCustomFee')

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
        reject(new Error(res.msg || '导入自定义费用失败'))
      }   
     }).catch(error => {
      reject(error.response.data)
    })
  })
}

// 查询费用规则列表
export function listPayFeeRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/payFeeRule.listPayFeeRule',
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