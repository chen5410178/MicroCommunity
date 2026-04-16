<template>
  <el-dialog
    :title="$t('common.confirm')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose">
    <div class="confirm-content">
      <i class="el-icon-warning" style="color: #E6A23C; font-size: 24px; margin-right: 10px;"></i>
      <span>{{ $t('common.deleteConfirm') }}</span>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMarketGoodsItem } from '@/api/market/marketGoodsItemManageApi'

export default {
  name: 'DeleteMarketGoodsItem',
  data() {
    return {
      visible: false,
      form: {
        itemId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form.itemId = data.itemId
      this.visible = true
    },
    handleClose() {
      this.form.itemId = ''
    },
    async handleConfirm() {
      try {
        await deleteMarketGoodsItem(this.form)
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

<style scoped>
.confirm-content {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}
</style>