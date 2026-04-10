<template>
  <el-dialog
    :title="$t('couponPoolManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center">
      <p>{{ $t('couponPoolManage.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('couponPoolManage.delete.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('couponPoolManage.delete.confirmDelete') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponPool } from '@/api/account/couponPoolManageApi'

export default {
  name: 'DeleteCouponPool',
  data() {
    return {
      visible: false,
      poolId: ''
    }
  },
  methods: {
    open(data) {
      this.poolId = data.poolId
      this.visible = true
    },
    handleClose() {
      this.poolId = ''
    },
    async handleSubmit() {
      try {
        await deleteCouponPool({ poolId: this.poolId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('couponPoolManage.delete.error'))
      }
    }
  }
}
</script>