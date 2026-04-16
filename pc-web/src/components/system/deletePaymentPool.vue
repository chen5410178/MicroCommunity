<template>
  <el-dialog
    :title="$t('paymentPool.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color:#E6A23C;font-size:24px;margin-right:10px;"></i>
      <span>{{ $t('paymentPool.delete.confirmText') }}</span>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="handleConfirm"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deletePaymentPool } from '@/api/system/paymentPoolApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePaymentPool',
  data() {
    return {
      visible: false,
      ppId: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(row) {
      this.visible = true
      this.ppId = row.ppId
    },
    handleClose() {
      this.ppId = ''
    },
    async handleConfirm() {
      try {
        await deletePaymentPool({
          ppId: this.ppId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('paymentPool.delete.error'))
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
  padding: 20px 0;
  font-size: 16px;
}
</style>