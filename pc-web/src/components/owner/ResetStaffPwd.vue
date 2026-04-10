<template>
  <el-dialog :title="$t('staff.confirmOperation')" :visible.sync="visible" width="30%" @close="closeDialog">
    <div class="text-center">
      <p>{{ $t('staff.confirmResetPassword') }}</p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmReset">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { resetStaffPwd } from '@/api/owner/auditAuthOwnerApi'

export default {
  name: 'ResetPwdDialog',
  data() {
    return {
      visible: false,
      currentStaff: null
    }
  },
  methods: {
    open(staff) {
      this.currentStaff = staff
      this.visible = true
    },
    closeDialog() {
      this.visible = false
      this.currentStaff = null
    },
    async confirmReset() {
      try {
        if (!this.currentStaff) return

        const params = {
          staffId: this.currentStaff.userId
        }

        const res = await resetStaffPwd(params)
        this.$message.success(
          this.$t('common.resetPwdSuccess', { pwd: res.pwd }),
          10000
        )
        this.closeDialog()
      } catch (error) {
        console.error('重置密码失败:', error)
        this.$message.error(error)
      }
    }
  }
}
</script>