<template>
  <div class="update-pwd-container">
    <el-card>
    <div class="update-pwd-title">{{ $t('updatePwd.title') }}</div>
    <el-form ref="pwdForm" :model="form" :rules="rules" label-width="100px" class="update-pwd-form">
      <el-form-item :label="$t('updatePwd.oldPassword.label')" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" :placeholder="$t('updatePwd.oldPassword.placeholder')"
          show-password />
      </el-form-item>
      <el-form-item :label="$t('updatePwd.newPassword.label')" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" :placeholder="$t('updatePwd.newPassword.placeholder')"
          show-password />
      </el-form-item>
      <el-form-item :label="$t('updatePwd.confirmPassword.label')" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password"
          :placeholder="$t('updatePwd.confirmPassword.placeholder')" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">{{ $t('updatePwd.button') }}</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  </div>
</template>

<script>
import { updatePassword } from '@/api/user/loginApi'

export default {
  name: 'UpdatePwd',
  data() {
    // 验证确认密码
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('updatePwd.confirmPassword.placeholder')))
      } else if (value !== this.form.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      form: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      rules: {
        oldPassword: [
          { required: true, message: this.$t('updatePwd.oldPassword.placeholder'), trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: this.$t('updatePwd.newPassword.placeholder'), trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.pwdForm.validate(async (valid) => {
        if (valid) {
          try {
            const { code, msg } = await updatePassword({
              oldPwd: this.form.oldPassword,
              newPwd: this.form.newPassword,
              reNewPwd: this.form.confirmPassword
            })
            if (code != 0) {
              this.$message.error(msg)
              return;
            }
            this.$message.success('密码修改成功')
            this.$refs.pwdForm.resetFields()
          } catch (error) {
            this.$message.error(error)
            console.error('修改密码失败:', error)
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.update-pwd-container {
  padding: 20px;

  .update-pwd-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 20px;
  }

  .update-pwd-form {
    width: 400px;
    margin: 0 auto;

    .el-form-item {
      margin-bottom: 20px;

      .el-input {
        width: 100%;
      }

      &:last-child {
        margin-bottom: 0;
        text-align: center;

        .el-button {
          width: 120px;
        }
      }
    }
  }
}
</style>