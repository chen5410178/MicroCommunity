<template>
  <el-dialog
    :title="$t('printerRule.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('printerRule.deleteConfirm') }}</p>
      <p class="delete-item">{{ form.ruleName }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleDelete">{{ $t('common.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePrinterRule } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePrinterRule',
  data() {
    return {
      visible: false,
      form: {
        ruleId: '',
        ruleName: '',
        communityId: ''
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.visible = true
      this.form = {
        ruleId: data.ruleId,
        ruleName: data.ruleName,
        communityId: getCommunityId()
      }
    },
    async handleDelete() {
      try {
        await deletePrinterRule(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除打印机规则失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
.delete-item {
  font-weight: bold;
  margin: 10px 0;
  color: #f56c6c;
}
</style>