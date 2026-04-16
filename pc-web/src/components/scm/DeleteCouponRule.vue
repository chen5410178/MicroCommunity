<template>
  <el-dialog
    :title="$t('couponRule.deleteConfirm')"
    :visible.sync="visible"
    width="30%"
    center>
    <div class="text-center">
      <p>{{ $t('couponRule.deleteRulePrompt') }}</p>
      <p class="delete-item">{{ currentRule.ruleName }}</p>
    </div>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('common.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteCouponRule } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteCouponRule',
  data() {
    return {
      visible: false,
      currentRule: {
        ruleId: '',
        ruleName: ''
      }
    }
  },
  methods: {
    open(data) {
      this.currentRule = {
        ruleId: data.ruleId,
        ruleName: data.ruleName,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteCouponRule(this.currentRule)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to delete coupon rule:', error)
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