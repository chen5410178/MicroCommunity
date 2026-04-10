<template>
  <el-dialog
    :title="$t('finishFeeRule.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <p>{{ $t('finishFeeRule.confirmMessage') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="finishFeeRule">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updatePayFeeRule } from '@/api/fee/finishFeeRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FinishFeeRule',
  data() {
    return {
      visible: false,
      finishFeeRuleInfo: {}
    }
  },
  methods: {
    open(params) {
      this.finishFeeRuleInfo = params
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async finishFeeRule() {
      const data = {
        ruleId: this.finishFeeRuleInfo.ruleId,
        state: '2009001',
        communityId: getCommunityId()
      }
      
      try {
        const res = await updatePayFeeRule(data)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error(this.$t('common.operateFail'))
      }
    }
  }
}
</script>