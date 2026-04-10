<template>
  <el-dialog
    :title="$t('resourceStoreManage.confirmOperation')"
    :visible.sync="dialogVisible"
    width="30%"
    :close-on-click-modal="false"
  >
    <div class="confirm-content">
      <p>{{ $t('resourceStoreManage.confirmDeleteResource') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        {{ $t('resourceStoreManage.cancelDelete') }}
      </el-button>
      <el-button type="primary" @click="confirmDelete">
        {{ $t('resourceStoreManage.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteResourceStore } from '@/api/resource/resourceStoreManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteResourceStore',
  data() {
    return {
      dialogVisible: false,
      resourceData: null
    }
  },
  methods: {
    open(resource) {
      this.resourceData = { ...resource }
      this.dialogVisible = true
    },
    
    async confirmDelete() {
      if (!this.resourceData) return
      
      try {
        const params = {
          ...this.resourceData,
          communityId: getCommunityId()
        }
        await deleteResourceStore(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除物品失败:', error)
        this.$message.error(error)
      }
    }
  }
}
</script>

<style scoped>
.confirm-content {
  text-align: center;
  font-size: 16px;
  margin: 20px 0;
}
</style>