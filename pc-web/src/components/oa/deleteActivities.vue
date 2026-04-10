<template>
  <el-dialog
    :title="$t('activitiesManage.delete.title')"
    :visible.sync="dialogVisible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning warning-icon"></i>
      <span>{{ $t('activitiesManage.delete.confirmText') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteActivities } from '@/api/oa/activitiesManageApi'

export default {
  name: 'DeleteActivities',
  data() {
    return {
      dialogVisible: false,
      activitiesId: ''
    }
  },
  methods: {
    open(row) {
      this.activitiesId = row.activitiesId
      this.dialogVisible = true
    },
    async handleConfirm() {
      try {
        const params = {
          activitiesId: this.activitiesId,
          communityId: getCommunityId()
        }
        await deleteActivities(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除活动失败:', error)
        this.$message.error(this.$t('common.deleteFailed'))
      }
    },
    handleClose() {
      this.activitiesId = ''
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  font-size: 16px;
  
  .warning-icon {
    color: #E6A23C;
    font-size: 24px;
    margin-right: 10px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>