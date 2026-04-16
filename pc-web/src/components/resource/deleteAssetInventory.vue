<template>
  <el-dialog
    :title="$t('assetInventoryManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('assetInventoryManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteAssetInventory } from '@/api/resource/assetInventoryManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteAssetInventory',
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
          ...this.currentData,
          communityId
        }
        await deleteAssetInventory(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error(this.$t('common.deleteFailed'))
      }
    },
    handleClose() {
      this.visible = false
      this.currentData = null
    }
  }
}
</script>