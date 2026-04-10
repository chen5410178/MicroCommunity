<template>
  <el-dialog
    :title="$t('common.confirm')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('common.deleteConfirm') }}</p>
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
import { deleteReportCustomComponent } from '@/api/report/reportCustomComponentManageApi'

export default {
  name: 'DeleteReportCustomComponent',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    componentId: {
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
        await deleteReportCustomComponent({ componentId: this.componentId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.$emit('update:visible', false)
      } catch (error) {
        if (error.message) {
          this.$message.error(error.message)
        }
      }
    }
  }
}
</script>