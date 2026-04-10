<template>
  <el-dialog
    :title="$t('serviceProvideManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center;">
      <p>{{ $t('serviceProvideManage.delete.confirm') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('serviceProvideManage.delete.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="handleConfirm"
      >
        {{ $t('serviceProvideManage.delete.confirmButton') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteServiceProvide } from '@/api/dev/serviceProvideManageApi'

export default {
  name: 'DeleteServiceProvide',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    serviceId: {
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
        await deleteServiceProvide({ id: this.serviceId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message || this.$t('serviceProvideManage.delete.error'))
      }
    }
  }
}
</script>