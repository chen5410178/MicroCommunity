<!-- components/staff/resetStaffPwd.vue -->
<template>
  <el-dialog :title="$t('staff.confirmOperation')" :visible.sync="visible" width="30%" @close="handleClose">
    <div class="text-center">
      <p>{{ $t('staff.confirmResetPassword') }}?</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('staff.wrongClick') }}</el-button>
      <el-button type="primary" @click="resetStaffPwd">{{ $t('staff.confirmReset') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { resetStaffPwd } from '@/api/staff/staffApi'

export default {
  name: 'ResetStaffPwd',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    staffInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.staffInfo = { ...row }
    },
    handleClose() {
      this.visible = false
      this.$emit('update:visible', false)
    },
    async resetStaffPwd() {
      try {
        this.loading = true
        const data = {
          communityId: '-1',
          staffId: this.staffInfo.userId
        }

        const res = await resetStaffPwd(data)
        this.$message.success(
          this.$t('common.resetPwdSuccess', { pwd: res.pwd }),
          10000
        )
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
