<template>
  <el-dialog
    :title="$t('couponPropertyPoolDetail.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('couponPropertyPoolDetail.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponPropertyPoolDetail } from '@/api/scm/couponPropertyPoolDetailApi'

export default {
  name: 'DeleteCouponPropertyPoolDetail',
  data() {
    return {
      visible: false,
      currentRow: null
    }
  },
  methods: {
    open(row) {
      this.currentRow = row
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.currentRow = null
    },
    async handleConfirm() {
      try {
        await deleteCouponPropertyPoolDetail(this.currentRow)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('couponPropertyPoolDetail.delete.error'))
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>