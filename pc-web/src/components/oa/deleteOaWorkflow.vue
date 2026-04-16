<template>
  <el-dialog
    :title="$t('oaWorkflowManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('oaWorkflowManage.delete.confirmText') }}</p>
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
import { deleteOaWorkflow } from '@/api/oa/oaWorkflowManageApi'

export default {
  name: 'DeleteOaWorkflow',
  data() {
    return {
      visible: false,
      flowId: ''
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.flowId = data.flowId
    },
    handleClose() {
      this.flowId = ''
    },
    async handleConfirm() {
      try {
        await deleteOaWorkflow({ flowId: this.flowId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('oaWorkflowManage.delete.error'))
      }
    }
  }
}
</script>