<!-- components/staff/deleteStaff.vue -->
<template>
  <el-dialog
    :title="$t('staff.confirmOperation')"
    :visible.sync="visible"
    width="40%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('staff.confirmDelete') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('staff.wrongClick') }}</el-button>
      <el-button type="danger" @click="deleteStaff">{{ $t('staff.confirmDeleteAction') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteStaff } from '@/api/staff/staffApi'

export default {
  name: 'DeleteStaff',
  data() {
    return {
      loading: false,
      staffInfo: {},
      visible: false
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
      this.visible = false
    },
    open(staffInfo) {
      this.staffInfo = staffInfo
      this.visible = true
    },
    async deleteStaff() {
      try {
        this.loading = true
        await deleteStaff({
          userId: this.staffInfo.userId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
