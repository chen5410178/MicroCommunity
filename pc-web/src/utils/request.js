import axios from 'axios'
import {  Loading } from 'element-ui'
import config from '@/conf/config'
import { getHeader } from './header'

// 创建loading实例
let loadingInstance = null

// 创建axios实例
const service = axios.create({
  baseURL: '/', // 基础URL
  timeout: config.apiTimeout // 请求超时时间
})

// 显示加载动画
const showLoading = () => {
  loadingInstance = Loading.service({
    lock: true,
    text: '加载中...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })
}

// 隐藏加载动画
const hideLoading = () => {
  if (loadingInstance) {
    loadingInstance.close()
    loadingInstance = null
  }
}

// 请求拦截器
service.interceptors.request.use(
  config => {
    let _header = getHeader();
    // 在发送请求之前做些什么
    config.headers['Content-Type'] = 'application/json'
    config.headers['Accept'] = 'application/json'
    config.headers['app-id'] = _header['app-id']
    config.headers['TRANSACTION-ID'] = _header['TRANSACTION-ID']
    config.headers['REQ-TIME'] = _header['REQ-TIME']
    config.headers['SIGN'] = _header['SIGN']
    config.headers['user-id'] = _header['user-id']
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    // 这里可以添加token等认证信息
    config.headers['Authorization'] = _header['Authorization']
    if (config.url.indexOf('/app/') == -1 && config.url.indexOf('/callComponent/') == -1) {
      config.baseURL = "/app"
    }
    // GET请求不显示加载动画，其他请求显示
    if (config.method !== 'get' && config.showLoading !== false) {
      showLoading()
    }

    return config
  },
  error => {
    console.log(error)
    // 请求错误时也要隐藏加载动画
    hideLoading()
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 隐藏加载动画
    hideLoading()
      return response
  },
  error => {
    console.log(error)

    // 隐藏加载动画
    hideLoading()

    // 判断是否为401未授权错误
    if (error.response && error.response.status === 401) {
      // 清除本地存储的token
      localStorage.removeItem('token')
      // 跳转到登录页面
      window.location.href = '/#/views/user/login'

      // Message({
      //   message: '登录已过期，请重新登录',
      //   type: 'error',
      //   duration: 5 * 1000
      // })
    }

    return Promise.reject(error)
  }
)

export default service 