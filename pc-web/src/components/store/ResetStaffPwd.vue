<template>
  <el-dialog
    :title="$t('propertyCompanyManage.confirmOperation')"
    :visible.sync="visible"
    width="30%"
  >
    <div>{{ $t('propertyCompanyManage.confirmResetPwd') }}</div>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { resetStaffPwd } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'ResetStaffPwd',

  data() {
    return {
      visible: false,
      staffInfo: {}
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.staffInfo = row.staffInfo
    },
    async handleConfirm() {
      try {
        const { pwd } = await resetStaffPwd({
          staffId: this.staffInfo.staffId
        })
        this.$message.success(
          this.$t('propertyCompanyManage.resetPwdSuccess', { pwd: pwd }),
          10000
        )
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>