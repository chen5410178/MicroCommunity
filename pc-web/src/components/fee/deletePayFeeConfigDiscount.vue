<template>
  <el-dialog
    :title="$t('deletePayFeeConfigDiscount.title')"
    :visible.sync="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <div class="text-center">
      <p>{{ $t('deletePayFeeConfigDiscount.confirmText') }}</p>
    </div>
    
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('deletePayFeeConfigDiscount.cancel') }}</el-button>
      <el-button type="primary" @click="deletePayFeeConfigDiscount" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deletePayFeeConfigDiscount } from '@/api/fee/payFeeConfigDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePayFeeConfigDiscount',
  data() {
    return {
      visible: false,
      loading: false,
      configDiscount: {}
    }
  },
  methods: {
    open(configDiscount) {
      this.configDiscount = { ...configDiscount }
      this.configDiscount.communityId = getCommunityId()
      this.visible = true
    },
    async deletePayFeeConfigDiscount() {
      try {
        this.loading = true
        await deletePayFeeConfigDiscount(this.configDiscount)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('deletePayFeeConfigDiscount.deleteError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>