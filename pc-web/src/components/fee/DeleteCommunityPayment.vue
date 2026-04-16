<template>
  <el-dialog
    :title="$t('deleteCommunityPayment.title')"
    :visible.sync="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color: #e6a23c; font-size: 24px; margin-right: 10px"></i>
      <span>{{ $t('deleteCommunityPayment.message') }}</span>
    </div>
    
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('communityPayment.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('communityPayment.confirmDelete') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteAdminPaymentPool } from '@/api/fee/communityPaymentApi'

export default {
  name: 'DeleteCommunityPayment',
  data() {
    return {
      visible: false,
      paymentId: ''
    }
  },
  methods: {
    open(row) {
      this.paymentId = row.ppId
      this.visible = true
    },
    async confirmDelete() {
      try {
        await deleteAdminPaymentPool({ ppId: this.paymentId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  font-size: 16px;
}
</style>