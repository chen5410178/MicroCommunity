import request from '@/utils/request'

export function importData(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data,
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