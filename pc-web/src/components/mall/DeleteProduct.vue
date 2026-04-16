<template>
  <el-dialog
    :title="$t('deleteProduct.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div style="text-align: center;">
      <p>{{ $t('deleteProduct.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('deleteProduct.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('deleteProduct.confirmBtn') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteProduct } from '@/api/mall/productSjApi'

export default {
  name: 'DeleteProduct',
  data() {
    return {
      visible: false,
      productId: ''
    }
  },
  methods: {
    open(productId) {
      this.productId = productId
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async confirmDelete() {
      try {
        await deleteProduct({ productId: this.productId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('deleteProduct.error'))
      }
    }
  }
}
</script>