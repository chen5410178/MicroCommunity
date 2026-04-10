<template>
  <el-dialog
    :title="$t('feeDiscountManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('feeDiscountManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="deleteFeeDiscount">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteFeeDiscount } from '@/api/fee/feeDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFeeDiscount',
  data() {
    return {
      visible: false,
      deleteFeeDiscountInfo: {
        discountId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.getCommunityId()
      this.deleteFeeDiscountInfo.discountId = row.discountId
    },
    async getCommunityId() {
      try {
        const communityId = await getCommunityId()
        this.deleteFeeDiscountInfo.communityId = communityId
      } catch (error) {
        console.error('获取communityId失败:', error)
      }
    },
    async deleteFeeDiscount() {
      try {
        await deleteFeeDiscount(this.deleteFeeDiscountInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
        this.resetForm()
      } catch (error) {
        console.error('删除折扣信息失败:', error)
      }
    },
    resetForm() {
      this.deleteFeeDiscountInfo = {
        discountId: '',
        communityId: this.deleteFeeDiscountInfo.communityId
      }
    },
    handleClose() {
      this.resetForm()
    }
  }
}
</script>