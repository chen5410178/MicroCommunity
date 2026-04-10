<template>
  <el-dialog
    :title="$t('systemUser.resetPwd')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose">
    <div style="text-align: center;">
      <p>{{ $t('systemUser.resetPwd') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="resetSystemUserPwd">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { resetSystemUserPwd } from '@/api/staff/systemUserApi'

export default {
  name: 'ResetSystemUserPwd',
  data() {
    return {
      visible: false,
      currentUser: null
    }
  },
  methods: {
    open(user) {
      this.currentUser = user
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async resetSystemUserPwd() {
      try {
        const res = await resetSystemUserPwd({ staffId: this.currentUser.userId })
        this.$message.success(
          this.$t('common.resetPwdSuccess', { pwd: res.pwd }),
          10000
        )
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error)
      }
    }
  }
}
</script>