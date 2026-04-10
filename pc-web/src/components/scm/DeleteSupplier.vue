<template>
  <el-dialog
    :title="$t('supplierManage.confirmDelete')"
    :visible.sync="visible"
    width="500px">
    <div class="text-center">
      <p>{{ $t('supplierManage.deleteTip') }}</p>
      <p class="mt-10"><strong>{{ supplier.supplierName }}</strong></p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('supplierManage.cancel') }}</el-button>
      <el-button type="primary" @click="deleteSupplier">{{ $t('supplierManage.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteSupplier } from '@/api/scm/supplierManageApi'

export default {
  name: 'DeleteSupplier',
  data() {
    return {
      visible: false,
      supplier: {}
    }
  },
  methods: {
    open(row) {
      this.supplier = { ...row }
      this.visible = true
    },
    
    async deleteSupplier() {
      try {
        const res = await deleteSupplier(this.supplier.supplierId)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg || this.$t('common.deleteFailed'))
        }
      } catch (error) {
        console.error('删除供应商失败:', error)
        this.$message.error(this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}

.mt-10 {
  margin-top: 10px;
}
</style>