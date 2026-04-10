<template>
  <el-dialog
    :title="$t('supplierTypeManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <p>{{ $t('supplierTypeManage.delete.confirm') }}</p>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteSupplierType } from '@/api/scm/supplierTypeManageApi'

export default {
  name: 'DeleteSupplierType',
  data() {
    return {
      visible: false,
      stId: ''
    }
  },
  methods: {
    open(row) {
      this.stId = row.stId
      this.visible = true
    },
    handleClose() {
      this.stId = ''
    },
    async handleConfirm() {
      try {
        await deleteSupplierType(this.stId)
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