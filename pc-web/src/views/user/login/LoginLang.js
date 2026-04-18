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
  },
  'zh-TW': {
    ...zhLocale,
    login: {
      title: 'HC智慧社區',
      username: '用戶名',
      password: '密碼',
      captcha: '驗證碼',
      login: '登錄',
      reset: '重置',
      usernamePlaceholder: '請輸入用戶名',
      passwordPlaceholder: '請輸入密碼',
      captchaPlaceholder: '請輸入驗證碼',
      loginFailed: '登錄失敗，請重試',
      getCaptchaFailed: '獲取驗證碼失敗',
      storeName:'公司名稱',
      relCdName:'角色'
    }
  }
}