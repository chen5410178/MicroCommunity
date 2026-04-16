<template>
  <el-dialog
    :title="$t('couponDetailManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color: #e6a23c; font-size: 24px;"></i>
      <span style="margin-left: 10px;">{{ $t('couponDetailManage.delete.confirm') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponDetail } from '@/api/account/couponDetailManageApi'

export default {
  name: 'DeleteCouponDetail',
  data() {
    return {
      visible: false,
      detailId: ''
    }
  },
  methods: {
    open(row) {
      this.detailId = row.detailId
      this.visible = true
    },
    handleClose() {
      this.detailId = ''
    },
    async handleConfirm() {
      try {
        await deleteCouponDetail({ detailId: this.detailId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}
</style>