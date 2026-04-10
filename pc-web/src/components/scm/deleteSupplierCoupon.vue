<template>
  <el-dialog :title="$t('supplierCoupon.deleteTitle')" :visible.sync="visible" width="30%">
    <p>{{ $t('supplierCoupon.deleteConfirm') }}</p>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('supplierCoupon.cancel') }}
      </el-button>
      <el-button type="primary" @click="confirmDelete">
        {{ $t('supplierCoupon.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteSupplierCoupon } from '@/api/scm/supplierCouponApi'

export default {
  name: 'DeleteSupplierCoupon',
  data() {
    return {
      visible: false,
      couponId: ''
    }
  },
  methods: {
    open(data) {
      this.couponId = data.couponId
      this.supplierId = data.supplierId
      this.visible = true
    },

    async confirmDelete() {
      try {
        await deleteSupplierCoupon({ couponId: this.couponId,supplierId:this.supplierId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('supplierCoupon.deleteFailed'))
      }
    }
  }
}
</script>