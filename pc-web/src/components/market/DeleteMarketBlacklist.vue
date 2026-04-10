<template>
  <el-dialog 
    :title="$t('marketBlacklist.delete.title')" 
    :visible.sync="visible" 
    width="30%"
  >
    <p>{{ $t('marketBlacklist.delete.confirm') }}</p>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMarketBlacklist } from '@/api/market/marketBlacklistManageApi'

export default {
  name: 'DeleteMarketBlacklist',
  data() {
    return {
      visible: false,
      formData: {}
    }
  },
  methods: {
    open(data) {
      this.formData = { ...data }
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMarketBlacklist(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>