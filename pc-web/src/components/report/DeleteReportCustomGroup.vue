<template>
  <el-dialog
    :title="$t('common.confirm')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('reportCustomGroupManage.delete.confirmText') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteReportCustomGroup } from '@/api/report/reportCustomGroupManageApi'

export default {
  name: 'DeleteReportCustomGroup',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    groupId: {
      type: String,
      default: ''
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
    },
    async handleConfirm() {
      try {
        await deleteReportCustomGroup({ groupId: this.groupId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>