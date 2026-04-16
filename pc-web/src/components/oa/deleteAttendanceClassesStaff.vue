<template>
  <el-dialog
    :title="$t('attendanceClassesStaffManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center;">
      <p>{{ $t('attendanceClassesStaffManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="deleteAttendanceClassesStaff" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteAttendanceClassesStaff } from '@/api/oa/attendanceClassesStaffManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteAttendanceClassesStaff',
  data() {
    return {
      visible: false,
      loading: false,
      currentRow: {}
    }
  },
  methods: {
    open(row) {
      this.currentRow = row
      this.visible = true
    },
    async deleteAttendanceClassesStaff() {
      try {
        this.loading = true
        const params = {
          ...this.currentRow,
          communityId: getCommunityId()
        }
        await deleteAttendanceClassesStaff(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('删除考勤员工失败:', error)
        this.$message.error(this.$t('attendanceClassesStaffManage.delete.error'))
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.currentRow = {}
    }
  }
}
</script>