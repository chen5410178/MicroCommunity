import request from '@/utils/request'

export function importData(data) {
  return new Promise(function (resolve, reject) {
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data: data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(function (response) {
      var res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(res.msg)
      }
    }).catch(function (error) {
      reject(error.response.data)
    })
  })
}

export function downloadTemplate(params) {
  return new Promise(function (resolve, reject) {
    request({
      url: '/export.exportData',
      method: 'get',
      params: params
    }).then(function (response) {
      var res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to download template'))
      }
    }).catch(function (error) {
      reject(error)
    })
  })
}
