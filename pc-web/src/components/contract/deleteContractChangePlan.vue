<template>
  <el-dialog
    :title="$t('deleteContractChangePlan.title')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="dialog-content">
      <p>{{ $t('deleteContractChangePlan.confirmText') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteContractChangePlan } from '@/api/contract/contractChangeManageApi'

export default {
  name: 'DeleteContractChangePlan',
  data() {
    return {
      visible: false,
      currentData: null
    }
  },
  methods: {
    open(data) {
      this.currentData = data
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteContractChangePlan(this.currentData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message || this.$t('deleteContractChangePlan.deleteError'))
      }
    },
    handleClose() {
      this.visible = false
      this.currentData = null
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-content {
  text-align: center;
  padding: 20px 0;
  
  p {
    font-size: 16px;
    color: #606266;
  }
}

.dialog-footer {
  text-align: right;
}
</style>