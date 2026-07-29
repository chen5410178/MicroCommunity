<template>
  <el-dialog
    :title="$t('refundDepositFee.title')"
    :visible.sync="dialogVisible"
    width="30%"
    @close="handleClose"
  >
    <el-alert
      :title="$t('refundDepositFee.alertText')"
      type="warning"
      :closable="false"
    ></el-alert>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="refundDepositFee">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { refundFeeDeposit } from '@/api/fee/payFeeOrderApi'

export default {
  name: 'RefundDepositFee',
  data() {
    return {
      dialogVisible: false,
      refundData: {}
    }
  },
  methods: {
    open(params) {
      this.refundData = { ...params }
      this.dialogVisible = true
    },
    close() {
      this.dialogVisible = false
    },
    async refundDepositFee() {
      try {
        this.refundData.communityId = await getCommunityId()
        
        const response = await refundFeeDeposit(this.refundData)
        if (response.code == 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.close()
          this.$emit('success')
        } else {
          this.$message.error(response.data.msg)
        }
      } catch (error) {
        console.error('退押金失败:', error)
        this.$message.error(this.$t('refundDepositFee.error'))
      }
    },
    handleClose() {
      this.refundData = {}
    }
  }
}
</script>

<style scoped>
.el-alert {
  margin-bottom: 20px;
}
.dialog-footer {
  text-align: right;
}
</style>