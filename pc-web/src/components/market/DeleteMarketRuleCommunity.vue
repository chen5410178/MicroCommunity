<template>
  <el-dialog :title="$t('marketRule.confirmDelete')" :visible.sync="visible" width="30%">
    <p>{{ $t('marketRule.deleteConfirmMessage') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('marketRule.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('marketRule.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMarketRuleCommunity } from '@/api/market/marketRuleApi'

export default {
  name: 'DeleteMarketRuleCommunity',
  data() {
    return {
      visible: false,
      communityId: '',
      rcId:''
    }
  },
  methods: {
    open(data) {
      this.communityId = data.communityId
      this.rcId = data.rcId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMarketRuleCommunity({ communityId: this.communityId , rcId: this.rcId})
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