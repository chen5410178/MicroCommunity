<template>
  <el-dialog
    :title="$t('layout.deleteConfirm')"
    :visible.sync="visible"
    width="400px"
    @close="handleClose"
  >
    <div class="del-content">
      <i class="el-icon-warning" style="color: #E6A23C; font-size: 24px; margin-right: 10px;"></i>
      <span>{{ $t('layout.deleteConfirm') }}</span>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('layout.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('layout.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMachine } from '@/api/machine/machineApi'

export default {
  name: 'DelMachine',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    machineId: {
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
        await deleteMachine(this.machineId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(this.$t('layout.deleteError'))
        }
      }
    }
  }
}
</script>

<style scoped>
.del-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}
</style> 