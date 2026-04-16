<template>
  <el-dialog
    :title="$t('deleteReportCustom.title')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div style="text-align: center">
      <p>{{ $t('deleteReportCustom.confirmDelete') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('deleteReportCustom.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('deleteReportCustom.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteReportCustom } from '@/api/report/reportCustomManageApi'

export default {
  name: 'DeleteReportCustom',
  props: {
    customId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      visible: false
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteReportCustom(this.customId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>