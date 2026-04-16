<template>
  <el-dialog
    :title="$t('communityPublicityManage.deleteTitle')"
    :visible.sync="visible"
    width="500px"
    :before-close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('communityPublicityManage.deleteConfirm') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteCommunityPublicity } from '@/api/community/communityPublicityManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteCommunityPublicity',
  data() {
    return {
      visible: false,
      loading: false,
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
    async handleConfirm() {
      try {
        this.loading = true
        const params = {
          pubId: this.currentItem.pubId,
          communityId: getCommunityId()
        }
        await deleteCommunityPublicity(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('删除失败:', error)
        this.$message.error(error.message || this.$t('communityPublicityManage.deleteError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}

.dialog-footer {
  text-align: right;
}
</style>