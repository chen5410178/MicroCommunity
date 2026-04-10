<template>
  <el-dialog
    :title="$t('service.dialog.delTitle')"
    :visible.sync="dialogVisible"
    width="400px"
    @close="handleClose"
  >
    <div class="del-dialog-content">
      <i class="el-icon-warning"></i>
      <span>{{ $t('service.delete.confirm') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('service.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('service.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteService } from '@/api/dev/serviceApi'

export default {
  name: 'DelService',
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
  data() {
    return {
      dialogVisible: false
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    async handleConfirm() {
      try {
        await deleteService(this.serviceId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('删除失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.del-dialog-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;

  i {
    font-size: 24px;
    color: #E6A23C;
    margin-right: 10px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>