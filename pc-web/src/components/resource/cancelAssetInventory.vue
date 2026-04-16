<template>
  <el-dialog
    :title="$t('assetInventoryManage.cancel.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('assetInventoryManage.cancel.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateAssetInventory } from '@/api/resource/assetInventoryManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CancelAssetInventory',
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
        const communityId = await getCommunityId()
        const params = {
          aiId: this.currentData.aiId,
          shId: this.currentData.shId,
          state: '1000',
          communityId
        }
        await updateAssetInventory(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('取消失败:', error)
        this.$message.error(this.$t('assetInventoryManage.cancel.failed'))
      }
    },
    handleClose() {
      this.visible = false
      this.currentData = null
    }
  }
}
</script>