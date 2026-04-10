<template>
  <el-dialog
    :title="$t('common.confirmDelete')"
    :visible.sync="visible"
    width="30%"
    center>
    <div class="text-center">
      <p>{{ $t('scheduleClassesStaffManage.confirmDeleteStaff') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteScheduleClassesStaff } from '@/api/org/scheduleClassesStaffManageApi'

export default {
  name: 'DeleteScheduleClassesStaff',
  data() {
    return {
      visible: false,
      deleteData: null
    }
  },
  methods: {
    open(data) {
      this.deleteData = data
      this.visible = true
    },
    async confirmDelete() {
      try {
        await deleteScheduleClassesStaff(this.deleteData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('common.deleteError'))
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
  margin: 20px 0;
}
</style>