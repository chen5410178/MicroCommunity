<template>
  <el-dialog
    :title="$t('payFeeQrcode.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <i class="el-icon-warning" style="font-size: 60px; color: #E6A23C;"></i>
      <p style="margin: 20px 0; font-size: 16px;">
        {{ $t('payFeeQrcode.delete.confirmText') }}
      </p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="handleConfirm"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePayFeeQrcode } from '@/api/fee/payFeeQrcodeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePayFeeQrcode',
  data() {
    return {
      visible: false,
      currentData: {
        pfqId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.currentData = {
        pfqId: data.pfqId,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.currentData = {
        pfqId: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        await deletePayFeeQrcode(this.currentData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('payFeeQrcode.message.deleteFailed'))
      }
    }
  }
}
</script>