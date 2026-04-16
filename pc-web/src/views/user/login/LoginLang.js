import enLocale from 'element-ui/lib/locale/lang/en'
import zhLocale from 'element-ui/lib/locale/lang/zh-CN'

export const messages = {
  en: {
    ...enLocale,
    login: {
      title: 'HC Smart Community',
      username: 'Username',
      password: 'Password',
      captcha: 'Verification Code',
      login: 'Login',
      reset: 'Reset',
      usernamePlaceholder: 'Please enter username',
      passwordPlaceholder: 'Please enter password',
      captchaPlaceholder: 'Please enter verification code',
      loginFailed: 'Login failed, please try again',
      getCaptchaFailed: 'Failed to get verification code',
      storeName:'store name',
      relCdName:'role'
    }
  },
  zh: {
    ...zhLocale,
    login: {
      title: 'HC智慧社区',
      username: '用户名',
      password: '密码',
      captcha: '验证码',
      login: '登录',
      reset: '重置',
      usernamePlaceholder: '请输入用户名',
      passwordPlaceholder: '请输入密码',
      captchaPlaceholder: '请输入验证码',
      loginFailed: '登录失败，请重试',
      getCaptchaFailed: '获取验证码失败',
      storeName:'公司名称',
      relCdName:'角色'
    }
  }
} 