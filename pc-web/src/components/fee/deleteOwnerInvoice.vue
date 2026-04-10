<template>
  <el-dialog
    :title="$t('confirmDeleteTitle')"
    :visible.sync="visible"
    width="30%"
  >
    <p>{{ $t('confirmDeleteContent') }}</p>
    <div slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="danger" @click="handleDelete">{{ $t('confirmDelete') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteOwnerInvoice } from '@/api/fee/ownerInvoiceApi'

export default {
  name: 'DeleteOwnerInvoice',
  data() {
    return {
      visible: false,
      currentData: null
    }
  },
  methods: {
    open(data) {
      this.currentData = data
      this.visible = true
    },
    async handleDelete() {
      try {
        await deleteOwnerInvoice({ oiId: this.currentData.oiId })
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