<template>
  <el-dialog
    :title="$t('deleteFeeRule.confirmTitle')"
    :visible.sync="dialogVisible"
    width="30%"
  >
    <div class="text-center">
      <p>{{ $t('deleteFeeRule.confirmMessage') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDeleteFeeRuleModel">{{ $t('deleteFeeRule.cancel') }}</el-button>
      <el-button type="primary" @click="deleteFeeRule">{{ $t('deleteFeeRule.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePayFeeRule } from '@/api/fee/deleteFeeRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFeeRule',
  data() {
    return {
      dialogVisible: false,
      deleteFeeRuleInfo: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.deleteFeeRuleInfo = params
      this.dialogVisible = true
    },
    deleteFeeRule() {
      const params = {
        ...this.deleteFeeRuleInfo,
        communityId: this.communityId
      }

      deletePayFeeRule(params).then(response => {
        if (response.code == 0) {
          this.dialogVisible = false
          this.$emit('success')
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(response.msg)
        }
      }).catch(error => {
        console.error('请求失败:', error)
        this.$message.error(this.$t('deleteFeeRule.deleteFailed'))
      })
    },
    closeDeleteFeeRuleModel() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>