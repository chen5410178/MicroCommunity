<template>
  <el-dialog :title="$t('propertyCompanyManage.passwordConfirm')" :visible.sync="visible" width="30%">
    <el-form>
      <el-form-item :label="$t('propertyCompanyManage.password')" required>
        <el-input v-model="password" type="password" :placeholder="$t('propertyCompanyManage.enterCurrentPassword')" />
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleLogin">{{ $t('propertyCompanyManage.login') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { adminLoginProperty } from '@/api/store/propertyCompanyManageApi'
import {  _loadStaffPrivileges } from '@/api/user/loginApi';
import { _loadCommunityInfo } from '@/api/community/communityApi';

export default {
  name: 'AdminLoginProperty',

  data() {
    return {
      password: '',
      visible: false,
      propertyInfo: {}
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.propertyInfo = row.staffInfo  
    },
    async handleLogin() {
      if (!this.password) {
        this.$message.warning(this.$t('propertyCompanyManage.passwordRequired'))
        return
      }

      try {
        const loginInfo = {
          username: this.propertyInfo.staffName,
          userId: this.propertyInfo.staffId,
          curPasswd: this.password,
          curUserName: this.$getUser().name
        }

       const {data} = await adminLoginProperty(loginInfo)
       this.visible = false

       this.handleSelect(data[0])

        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    async handleSelect(_userData) {
      localStorage.clear();
      localStorage.setItem('token', _userData.token);
      let _user = {
        userId: _userData.userId,
        name: _userData.name,
        tel: _userData.tel
      }
      localStorage.setItem('user', JSON.stringify(_user));
      await _loadStaffPrivileges();
      await _loadCommunityInfo();
      window.location.href = '/';
    },
  }
}
</script>