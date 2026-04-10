<template>
  <el-dialog
    :title="$t('deleteAttendanceClasses.title')"
    :visible.sync="visible"
    width="30%"
    @close="closeDialog"
  >
    <div class="confirm-content">
      <p>{{ $t('deleteAttendanceClasses.confirmText') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('deleteAttendanceClasses.cancel') }}</el-button>
      <el-button type="primary" @click="deleteAttendanceClasses">{{ $t('deleteAttendanceClasses.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteAttendanceClasses } from '@/api/oa/attendanceClassesManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteAttendanceClasses',
  data() {
    return {
      visible: false,
      formData: {
        classesId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.formData = {
        classesId: params.classesId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    closeDialog() {
      this.visible = false
    },
    async deleteAttendanceClasses() {
      try {
        const response = await deleteAttendanceClasses(this.formData)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.closeDialog()
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error(this.$t('deleteAttendanceClasses.deleteError'))
      }
    }
  }
}
</script>

<style scoped>
.confirm-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>