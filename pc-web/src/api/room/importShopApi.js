import request from '@/utils/request'

export function importShop(formData) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      resolve(response)
    }).catch(error => {
      reject(error.response.data)
    })
  })
}
