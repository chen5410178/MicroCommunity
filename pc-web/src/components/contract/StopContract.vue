<template>
  <el-dialog
    :title="$t('stopContract.dialog.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div class="dialog-body">
      <p>{{ $t('stopContract.dialog.content') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('stopContract.button.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('stopContract.button.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { stopContract } from '@/api/contract/expirationContractManageApi'

export default {
  name: 'StopContract',
  data() {
    return {
      visible: false,
      currentContract: null
    }
  },
  methods: {
    open(contract) {
      this.currentContract = contract
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.currentContract = null
    },
    async handleConfirm() {
      try {
        await stopContract(this.currentContract)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('stopContract.message.error'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-body {
  text-align: center;
  padding: 20px 0;
  font-size: 16px;
}
</style>