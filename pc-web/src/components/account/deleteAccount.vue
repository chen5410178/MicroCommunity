<template>
  <el-dialog
    :title="$t('deleteAccount.title')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteAccount.confirmDelete') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('deleteAccount.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('deleteAccount.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteAccount } from '@/api/account/accountManageApi'

export default {
  name: 'DeleteAccount',
  data() {
    return {
      visible: false,
      accountData: {}
    }
  },
  methods: {
    open(account) {
      this.accountData = { ...account }
      this.visible = true
    },
    
    handleClose() {
      this.accountData = {}
    },
    
    async handleConfirm() {
      if (!this.accountData.acctId) return
      
      try {
        await deleteAccount({ acctId: this.accountData.acctId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteError'))
      }
    }
  }
}
</script>