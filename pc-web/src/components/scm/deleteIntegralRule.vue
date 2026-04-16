<template>
  <el-dialog
    :title="$t('common.confirmTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('integralRule.deleteConfirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteIntegralRule } from '@/api/scm/integralRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteIntegralRule',
  data() {
    return {
      visible: false,
      ruleId: '',
      communityId: ''
    }
  },
  methods: {
    open(data) {
      this.ruleId = data.ruleId
      this.communityId = getCommunityId()
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteIntegralRule({
          ruleId: this.ruleId,
          communityId: this.communityId
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