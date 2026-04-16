<template>
  <el-dialog
    :title="$t('deleteInvoiceApply.title')"
    :visible.sync="visible"
    width="30%"
    @close="closeDialog"
  >
    <div align="center">
      <p>{{ $t('deleteInvoiceApply.confirmMessage') }}</p>
    </div>
    <div slot="footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button
        type="primary"
        @click="deleteInvoiceApply"
      >{{ $t('common.confirmDelete') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteInvoiceApply as deleteApi } from '@/api/fee/invoiceApplyApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteInvoiceApply',
  data() {
    return {
      visible: false,
      applyData: {}
    }
  },
  methods: {
    open(row) {
      this.applyData = { ...row }
      this.visible = true
    },
    closeDialog() {
      this.visible = false
    },
    async deleteInvoiceApply() {
      try {
        const params = {
          ...this.applyData,
          communityId: getCommunityId()
        }
        await deleteApi(params)
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.closeDialog()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>