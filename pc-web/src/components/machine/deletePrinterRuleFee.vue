<template>
  <el-dialog
    :title="$t('printerRuleFees.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('printerRuleFees.deleteConfirm') }}</p>
      <p class="delete-item">{{ form.feeConfigName }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleDelete">{{ $t('common.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePrinterRuleFee } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePrinterRuleFee',
  data() {
    return {
      visible: false,
      form: {
        prfId: '',
        feeConfigName: '',
        communityId: ''
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    open(row) {
      this.visible = true
      this.form = {
        prfId: row.prfId,
        feeConfigName: row.feeConfigName,
        communityId: getCommunityId()
      }
    },
    async handleDelete() {
      try {
        await deletePrinterRuleFee({
          prfId: this.form.prfId,
          communityId: this.form.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除缴费自动打印失败:', error)
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