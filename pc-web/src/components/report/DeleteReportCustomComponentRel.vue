<template>
  <el-dialog
    :title="$t('deleteReportCustomComponentRel.title')"
    :visible.sync="visible"
    width="30%"
  >
    <span>{{ $t('deleteReportCustomComponentRel.confirmMessage') }}</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteReportCustomComponentRel } from '@/api/report/reportCustomComponentRelManageApi'

export default {
  name: 'DeleteReportCustomComponentRel',
  data() {
    return {
      visible: false,
      currentRow: null
    }
  },
  methods: {
    open(row) {
      this.currentRow = row
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteReportCustomComponentRel({
          relId: this.currentRow.relId,
          customId:this.currentRow.customId,
          communityId: '-1'
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('deleteReportCustomComponentRel.deleteError'))
      }
    }
  }
}
</script>