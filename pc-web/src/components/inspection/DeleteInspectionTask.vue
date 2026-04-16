<template>
  <el-dialog
    :title="$t('deleteInspectionTask.confirmOperation')"
    :visible.sync="visible"
    width="30%"
  >
    <div style="text-align: center;">
      <p>{{ $t('deleteInspectionTask.confirmDelete') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('deleteInspectionTask.mistake') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('deleteInspectionTask.confirmDeleteBtn') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteInspectionTask } from '@/api/inspection/inspectionTaskApi'

export default {
  name: 'DeleteInspectionTask',
  data() {
    return {
      visible: false,
      taskId: ''
    }
  },
  methods: {
    open(task) {
      this.taskId = task.taskId
      this.visible = true
    },
    close() {
      this.taskId = ''
      this.visible = false
    },
    async handleConfirm() {
      try {
        await deleteInspectionTask({ taskId: this.taskId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除任务失败:', error)
        this.$message.error(error.message || this.$t('common.deleteFail'))
      }
    }
  }
}
</script>