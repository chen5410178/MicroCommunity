<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose">
    <p>{{ $t('common.deleteConfirm') }}</p>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteReportCustomComponentFooter } from '@/api/report/reportCustomComponentFooterManageApi'

export default {
  name: 'DeleteReportCustomComponentFooter',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    footerId: {
      type: String,
      required: true
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
    },
    async handleConfirm() {
      try {
        await deleteReportCustomComponentFooter({ footerId: this.footerId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>