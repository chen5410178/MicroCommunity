<template>
  <el-dialog
    :title="$t('couponPropertyPoolManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align:center;margin-bottom:20px">
      <p>{{ $t('couponPropertyPoolManage.delete.confirmText') }}</p>
      <p style="font-weight:bold">{{ formData.couponName }}</p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponPropertyPool } from '@/api/scm/couponPropertyPoolManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteCouponPropertyPool',
  data() {
    return {
      visible: false,
      formData: {
        cppId: '',
        couponName: '',
        communityId: getCommunityId()
      }
    }
  },
  methods: {
    open(row) {
      this.formData = {
        ...this.formData,
        cppId: row.cppId,
        couponName: row.couponName
      }
      this.visible = true
    },
    handleClose() {
      this.formData.cppId = ''
      this.formData.couponName = ''
    },
    async handleConfirm() {
      try {
        await deleteCouponPropertyPool(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('couponPropertyPoolManage.delete.error'))
      }
    }
  }
}
</script>