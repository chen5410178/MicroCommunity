<template>
  <el-dialog
    :title="$t('examineProjectManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color:#E6A23C;font-size:24px;vertical-align:middle"></i>
      <span style="margin-left:10px;vertical-align:middle">
        {{ $t('examineProjectManage.delete.confirmText') }}
      </span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteExamineProject } from '@/api/oa/examineProjectManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteExamineProject',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        projectId: '',
        communityId: getCommunityId()
      }
    }
  },
  methods: {
    open(data) {
      this.formData.projectId = data.projectId
      this.visible = true
    },
    handleClose() {
      this.loading = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteExamineProject(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('examineProjectManage.delete.error'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  padding: 20px 0;
  text-align: center;
}
</style>