<template>
  <el-dialog
    :title="$t('couponRuleCpps.deleteConfirm')"
    :visible.sync="visible"
    width="30%"
    center>
    <div class="text-center">
      <p>{{ $t('couponRuleCpps.deletePrompt') }}</p>
      <p class="delete-item">{{ currentItem.couponName }}</p>
    </div>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('common.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponRuleCpps } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteCouponRuleCpps',
  data() {
    return {
      visible: false,
      currentItem: {
        crcId: '',
        couponName: ''
      }
    }
  },
  methods: {
    open(row) {
      this.currentItem = {
        crcId: row.crcId,
        couponName: row.couponName,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteCouponRuleCpps({
          crcId: this.currentItem.crcId,
          communityId: this.currentItem.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to delete coupon rule cpps:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.text-center {
  text-align: center;
  
  .delete-item {
    font-weight: bold;
    color: #f56c6c;
    margin-top: 10px;
  }
}
</style>