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
import { deleteMarketRuleObj } from '@/api/market/marketRuleApi'

export default {
  name: 'DeleteMarketRuleObj',
  data() {
    return {
      visible: false,
      objId: ''
    }
  },
  methods: {
    open(data) {
      this.objId = data.objId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMarketRuleObj({ objId: this.objId })
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