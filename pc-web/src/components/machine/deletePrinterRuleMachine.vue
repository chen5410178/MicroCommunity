<template>
  <el-dialog
    :title="$t('printerRuleMachine.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('printerRuleMachine.deleteConfirm') }}</p>
      <p class="delete-item">{{ form.machineName }}</p>
      <p>{{ $t('printerRuleMachine.quantity') }}: {{ form.quantity }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleDelete">{{ $t('common.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePrinterRuleMachine } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePrinterRuleMachine',
  data() {
    return {
      visible: false,
      form: {
        prmId: '',
        machineName: '',
        quantity: 0,
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
        prmId: row.prmId,
        machineName: row.machineName,
        quantity: row.quantity,
        communityId: getCommunityId()
      }
    },
    async handleDelete() {
      try {
        await deletePrinterRuleMachine({
          prmId: this.form.prmId,
          communityId: this.form.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除打印机规则机器失败:', error)
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