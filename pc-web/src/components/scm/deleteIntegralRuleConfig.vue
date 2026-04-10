<template>
  <el-dialog
    :title="$t('common.confirmTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('integralRule.deleteConfigConfirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button 
        type="danger" 
        @click="handleConfirm"
      >{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteIntegralRuleConfig } from '@/api/scm/integralRuleApi'

export default {
  name: 'DeleteIntegralRuleConfig',
  data() {
    return {
      visible: false,
      configId: '',
      ruleId: ''
    }
  },
  methods: {
    open(data) {
      this.configId = data.configId
      this.ruleId = data.ruleId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteIntegralRuleConfig({
          configId: this.configId,
          ruleId: this.ruleId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>