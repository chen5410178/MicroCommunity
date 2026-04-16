<template>
  <el-dialog
    :title="$t('marketRule.confirmDelete')"
    :visible.sync="visible"
    width="30%"
  >
    <p>{{ $t('marketRule.deleteConfirmMessage') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('marketRule.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('marketRule.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMarketRule } from '@/api/market/marketRuleApi'

export default {
  name: 'DeleteMarketRule',
  data() {
    return {
      visible: false,
      ruleId: ''
    }
  },
  methods: {
    open(data) {
      this.ruleId = data.ruleId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMarketRule({ ruleId: this.ruleId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>