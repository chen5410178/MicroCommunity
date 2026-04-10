<template>
  <el-dialog
    :title="$t('deleteItemOut.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteItemOut.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deletePurchaseApply } from '@/api/resource/itemOutManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteItemOut',
  data() {
    return {
      visible: false,
      currentItem: null
    }
  },
  methods: {
    open(item) {
      this.currentItem = item
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async confirmDelete() {
      try {
        const params = {
          ...this.currentItem,
          communityId: getCommunityId()
        }
        await deletePurchaseApply(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('删除失败:', error)
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