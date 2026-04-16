<template>
  <el-dialog 
    :title="$t('resourceAuditFlow.confirmTitle')" 
    :visible.sync="dialogVisible" 
    width="500px"
  >
    <div class="delete-content">
      <p>{{ $t('resourceAuditFlow.confirmDelete') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        {{ $t('resourceAuditFlow.cancel') }}
      </el-button>
      <el-button type="danger" @click="handleConfirm">
        {{ $t('resourceAuditFlow.confirmDeleteBtn') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteResourceAuditFlow } from '@/api/resource/resourceAuditFlowApi'

export default {
  name: 'DeleteResourceAuditFlow',
  data() {
    return {
      dialogVisible: false,
      currentRow: null
    }
  },
  methods: {
    open(row) {
      this.currentRow = { ...row }
      this.dialogVisible = true
    },
    async handleConfirm() {
      if (!this.currentRow) return
      
      try {
        const res = await deleteResourceAuditFlow({
          rafId: this.currentRow.rafId
        })
        
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.dialogVisible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg || this.$t('common.deleteFailed'))
        }
      } catch (error) {
        console.error('删除审批流程失败:', error)
        this.$message.error(this.$t('common.deleteFailed'))
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