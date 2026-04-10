<template>
  <el-dialog
    :title="$t('machinePrinterManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('machinePrinterManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMachinePrinter } from '@/api/machine/machinePrinterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMachinePrinter',
  data() {
    return {
      visible: false,
      form: {
        machineId: '',
        communityId: getCommunityId()
      }
    }
  },
  methods: {
    open(row) {
      this.form.machineId = row.machineId
      this.visible = true
    },
    handleClose() {
      this.form.machineId = ''
    },
    async handleConfirm() {
      try {
        await deleteMachinePrinter(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>