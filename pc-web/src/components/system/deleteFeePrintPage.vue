<template>
  <el-dialog
    :title="$t('feePrintPageManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color:#E6A23C;font-size:24px;vertical-align:middle;"></i>
      <span style="margin-left:10px;vertical-align:middle;">
        {{ $t('feePrintPageManage.delete.confirmText') }}
      </span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        :loading="loading"
        @click="handleConfirm"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteFeePrintPage } from '@/api/system/feePrintPageManageApi'

export default {
  name: 'DeleteFeePrintPage',
  data() {
    return {
      visible: false,
      loading: false,
      communityId: '',
      currentRow: {
        pageId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.communityId = getCommunityId()
      this.currentRow = {
        pageId: row.pageId,
        communityId: this.communityId
      }
      this.visible = true
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteFeePrintPage(this.currentRow)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('删除失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.currentRow = {
        pageId: '',
        communityId: ''
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  padding: 10px 20px;
  font-size: 16px;
}
</style>