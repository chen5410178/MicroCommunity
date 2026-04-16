<template>
  <el-dialog
    :title="$t('deleteStorehouseManage.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteStorehouseManage.confirmCancel') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('deleteStorehouseManage.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('deleteStorehouseManage.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteAllocationStorehouse } from '@/api/resource/allocationStorehouseManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteStorehouseManage',
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
        const params = {
          ...this.currentData,
          communityId: getCommunityId()
        }
        
        const res = await deleteAllocationStorehouse(params)
        if (res.code === 0) {
          this.$message.success(res.msg)
          this.$emit('success')
          this.visible = false
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('取消调拨失败:', error)
        this.$message.error(this.$t('deleteStorehouseManage.deleteError'))
      }
    },
    handleClose() {
      this.currentData = null
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
  font-size: 16px;
  margin: 20px 0;
}
</style>