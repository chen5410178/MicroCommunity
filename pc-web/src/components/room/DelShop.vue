<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('shops.confirmDelete')"
    width="400px"
    @close="handleClose"
  >
    <div style="text-align: center; padding: 20px 0;">
      <i class="el-icon-warning" style="font-size: 48px; color: #f56c6c;"></i>
      <p style="margin-top: 20px; font-size: 16px;">
        {{ $t('shops.deleteConfirmMsg') }}
      </p>
    </div>
    <span slot="footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleSubmit" :loading="loading">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteBuildingShop } from '@/api/room/buildingShopApi'

export default {
  name: 'DelShop',
  data() {
    return {
      visible: false,
      loading: false,
      shopId: ''
    }
  },
  methods: {
    open(shopData) {
      this.shopId = shopData.shopId
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    handleSubmit() {
      this.loading = true
      deleteBuildingShop(this.shopId)
        .then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        })
        .catch(error => {
          console.error(error)
        })
        .finally(() => {
          this.loading = false
        })
    }
  }
}
</script>