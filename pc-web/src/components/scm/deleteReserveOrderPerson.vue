<template>
  <el-dialog
    :title="$t('reserveOrder.cancelAppointment')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <el-form>
      <el-form-item :label="$t('reserveOrder.confirmCancel')">
        <span>{{ currentRow.orderId }}</span>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { cancelReserveOrder } from '@/api/scm/reserveOrderApi'

export default {
  name: 'DeleteReserveOrderPerson',
  data() {
    return {
      visible: false,
      loading: false,
      currentRow: {}
    }
  },
  methods: {
    open(row) {
      this.currentRow = { ...row }
      this.visible = true
    },
    handleClose() {
      this.currentRow = {}
    },
    async handleConfirm() {
      try {
        this.loading = true
        await cancelReserveOrder({ orderId: this.currentRow.orderId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('reserveOrder.cancelError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>