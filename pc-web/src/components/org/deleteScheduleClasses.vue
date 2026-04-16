<template>
  <el-dialog
    :title="$t('scheduleClasses.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center">
      <p>{{ $t('scheduleClasses.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteScheduleClasses } from '@/api/org/scheduleClassesApi'

export default {
  name: 'DeleteScheduleClasses',
  data() {
    return {
      visible: false,
      currentItem: null
    }
  },
  methods: {
    open(item) {
      this.currentItem = item
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteScheduleClasses(this.currentItem)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('scheduleClasses.delete.error'))
      }
    },
    handleClose() {
      this.visible = false
    }
  }
}
</script>