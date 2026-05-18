<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-title" v-if="logo">
        <img :src="logo" alt="logo">
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" :placeholder="$t('login.usernamePlaceholder')"></el-input>
        </el-form-item>
        <el-form-item prop="passwd">
          <el-input v-model="loginForm.passwd" type="password" :placeholder="$t('login.passwordPlaceholder')"></el-input>
        </el-form-item>
        <el-form-item prop="validateCode" class="captcha-item">
          <div class="captcha-container">
            <el-input v-model="loginForm.validateCode" :placeholder="$t('login.captchaPlaceholder')"></el-input>
            <img :src="captchaUrl" @click="refreshCaptcha" class="captcha-img" alt="验证码">
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')" :loading="loading">{{ $t('login.login')
          }}</el-button>
        </el-form-item>
      </el-form>
      <!-- Add a white line and copyright info below the form -->
      <div class="divider"></div>
      <div class="copyright">©2020-2026 <span v-html="companyName"></span></div>
    </div>
    <select-login-user ref="selectLoginUserRef" @select="handleSelect"></select-login-user>
  </div>
</template>

<script>
import { login, getCaptcha, _loadStaffPrivileges } from '@/api/user/loginApi';
import { _loadCommunityInfo } from '@/api/community/communityApi';
import {listSystemInfo} from '@/api/system/systemInfoManageApi'
import selectLoginUser from '@/components/user/selectLoginUser.vue';


export default {
  name: 'Login',
  data() {
    return {
      logo: '',
      companyName:'',
      loginForm: {
        username: '',
        passwd: '',
        validateCode: ''
      },
      captchaUrl: '',
      loading: false,
      rules: {
        username: [
          { required: true, message: this.$t('login.usernamePlaceholder'), trigger: 'blur' }
        ],
        passwd: [
          { required: true, message: this.$t('login.passwordPlaceholder'), trigger: 'blur' }
        ],
        validateCode: [
          { required: true, message: this.$t('login.captchaPlaceholder'), trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    localStorage.clear();
    this.getSystemInfo()
    setTimeout(() => {
      this.refreshCaptcha();
    }, 1000);
    
  },
  components: {
    selectLoginUser
  },
  methods: {
    async getSystemInfo() {
      const { data } = await listSystemInfo({ page: 1, row: 1 });
      this.logo = data[0].logoUrl
      this.companyName = data[0].companyName
    },
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.loading = true;
          try {
            const { data } = await login(this.loginForm).catch(error => {
              this.$message.error(error);
              this.refreshCaptcha();
              return { data: [] };  
            });
            if (!data || data.length == 0) {
              return;
            }

            if (data && data.length > 1) {
              this.$refs.selectLoginUserRef.openDialog(data);
              return;
            }
            this.handleSelect(data[0]);
          } catch (error) {
            console.error('登录失败：', error);
            this.refreshCaptcha();
          } finally {
            this.loading = false;
          }
        } else {
          return false;
        }
      });
    },
    async handleSelect(_userData) {
      localStorage.setItem('token', _userData.token);
      let _user = {
        userId: _userData.userId,
        name: _userData.name,
        tel: _userData.tel
      }
      localStorage.setItem('user', JSON.stringify(_user));
      await _loadStaffPrivileges();
      await _loadCommunityInfo();
      this.$router.push('/');
    },
    async refreshCaptcha() {
      try {
        const res = await getCaptcha();
        this.captchaUrl = res;
      } catch (error) {
        this.$message.error(this.$t('login.getCaptchaFailed'));
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: url('/img/login-bg_x.png') no-repeat center center;
  background-size: cover;
  position: relative;
  overflow: hidden;
}

.login-card {
  width: 300px;
  padding: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  position: absolute;
  left: 50%;
  top: 35%;
  margin: -150px 0 0 -150px;
  z-index: 99;
  text-align: center;
  padding: 30px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.login-card .login-title {
  color: #fff;
  text-align: center;
  font-size: 48px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input__inner {
  background: #fff;
  border: none;
  border-radius: 5px;
  color: #333;
  height: 40px;
}

.captcha-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.captcha-item .el-input {
  width: 70%;
  height: 38px;
  line-height: 1.3;
  border-width: 1px;
  border-style: solid;
  background-color: #fff;
  border-radius: 2px;
}

.captcha-img {
  height: 40px;
  cursor: pointer;
  border-radius: 5px;
}

.el-button--primary {
  width: 100%;
  background: #1E9FFF;
  border: none;
  border-radius: 100px;
  font-size: 14px;
  height: 38px;
}

.el-button--primary:hover {
  opacity: 0.8;
  color: #fff;
}

/* Style for the white line */
.divider {
  width: 100%;
  height: 1px;
  background-color: #fff;
  margin: 10px 0;
}

/* Style for the copyright text */
.copyright {
  color: #fff;
  font-size: 14px;
  text-align: center;
  margin-top: 10px;
}
</style>