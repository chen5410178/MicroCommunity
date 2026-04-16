<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    @close="closeDialog"
  >
    <div class="text-center">
      <p>{{ $t('common.deleteConfirm') }}</p>
    </div>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteAppUserBindingOwner } from '@/api/owner/auditAuthOwnerApi'

export default {
  name: 'DeleteBindingOwnerDialog',
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
    closeDialog() {
      this.visible = false
      this.currentData = null
    },
    async confirmDelete() {
      try {
        if (!this.currentData) return
        
        const params = {
          appUserId: this.currentData.appUserId,
          roomId: this.currentData.roomId
        }
        
        await deleteAppUserBindingOwner(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('delete-success')
        this.closeDialog()
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error(error)
      }
    }
  }
}
</script>