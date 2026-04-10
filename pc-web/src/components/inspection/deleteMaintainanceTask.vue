<template>
  <el-dialog
    :title="$t('deleteMaintainanceTask.title')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="confirm-content">
      <i class="el-icon-warning warning-icon"></i>
      <span>{{ $t('deleteMaintainanceTask.confirmText') }}</span>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('deleteMaintainanceTask.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('deleteMaintainanceTask.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMaintainanceTask } from '@/api/inspection/maintainanceTaskManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMaintainanceTask',
  data() {
    return {
      visible: false,
      loading: false,
      task: {
        taskId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.task = {
        taskId: data.taskId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.task = {
        taskId: '',
        communityId: ''
      }
    },
    handleConfirm() {
      this.loading = true
      deleteMaintainanceTask(this.task)
        .then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
        })
        .catch(error => {
          this.$message.error(error.message || this.$t('deleteMaintainanceTask.error'))
        })
        .finally(() => {
          this.loading = false
        })
    }
  }
}
</script>

<style scoped>
.confirm-content {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.warning-icon {
  color: #e6a23c;
  font-size: 24px;
  margin-right: 10px;
}
</style>