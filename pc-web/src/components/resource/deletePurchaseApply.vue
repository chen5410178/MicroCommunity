<template>
  <el-dialog
    :title="$t('purchaseApplyManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('purchaseApplyManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePurchaseApply } from '@/api/resource/purchaseApplyManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeletePurchaseApply',
  data() {
    return {
      visible: false,
      form: {
        applyOrderId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        applyOrderId: data.applyOrderId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.form = {
        applyOrderId: '',
        communityId: ''
      }
    },
    async handleConfirm() {
      try {
        await deletePurchaseApply(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error(this.$t('purchaseApplyManage.delete.error'))
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>