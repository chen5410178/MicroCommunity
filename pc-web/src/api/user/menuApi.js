import request from '@/utils/request'
import { getCommunityId } from "@/utils/vc"
import { Message } from 'element-ui'
export function _getMenuCatalog() {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.listCatalog',
      method: 'get',
      params: {
        page: 1,
        row: 10,
        isShow: 'Y',
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取项目列表
export function getMenuList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.listMenu',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(response)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加项目
export function addMenu(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.saveMenu',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新项目
export function updateMenu(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.updateMenu',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除项目
export function deleteMenu(mId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/menu.deleteMenu',
      method: 'post',
      data: { mId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取项目列表
export function getMenuTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/menu.listCatalogMenus',
      method: 'get',
      params: {
        caId: params.caId,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function jumpToMall(url) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getMallToken',
      method: 'get',
      params: {
        targetUrl: encodeURIComponent(url),
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        let _url = res.data.url;
        window.open(_url);
        resolve(res)
      } else {
        Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        })
        reject(new Error(res.msg || '跳转失败'))
      }
    }).catch(error => {
      Message({
        message: error,
        type: 'error',
        duration: 5 * 1000
      })
      reject(error)
    })
  })
}

export function jumpToIot(url) {
  return new Promise((resolve, reject) => {
    request({
      url: '/iot.getIotToken',
      method: 'get',
      params: {
        targetUrl: encodeURIComponent(url),
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        let _url = res.data.url;
        window.open(_url);
        resolve(res)
      } else {
        Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        })
        reject(new Error(res.msg || '跳转失败'))
      }
    }).catch(error => {
      Message({
        message: error,
        type: 'error',
        duration: 5 * 1000
      })
      reject(error)
    })
  })
}

export function jumpToPlugin(url) {

  return new Promise((resolve, reject) => {
    request({
      url: '/plugin.getPluginToken',
      method: 'get',
      params: {
        targetUrl: encodeURIComponent(url),
        communityId: getCommunityId()
      }
    }).then(response => {
      //发送get请求
      const res = response.data
      if (res.code != 0) {
        Message({
          message: res.msg,
          type: 'error',
          duration: 5 * 1000
        })
        return;
      }
      let _url = res.data.url;
      window.open(_url);
      resolve(res)
    }).catch(error => {
      Message({
        message: error,
        type: 'error',
        duration: 5 * 1000
      })
      reject(error)
    })
  })
}