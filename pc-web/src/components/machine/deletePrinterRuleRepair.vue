<template>
  <el-dialog
    :title="$t('printerRuleRepair.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('printerRuleRepair.deleteConfirm') }}</p>
      <p class="delete-item">{{ form.repairTypeName }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleDelete">{{ $t('common.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePrinterRuleRepair } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePrinterRuleRepair',
  data() {
    return {
      visible: false,
      form: {
        prrId: '',
        repairTypeName: '',
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
        prrId: row.prrId,
        repairTypeName: row.repairTypeName,
        communityId: getCommunityId()
      }
    },
    async handleDelete() {
      try {
        await deletePrinterRuleRepair({
          prrId: this.form.prrId,
          communityId: this.form.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除报修自动打印失败:', error)
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