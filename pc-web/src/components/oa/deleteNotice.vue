<template>
  <el-dialog
    :title="$t('noticeManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align:center">
      <p>{{ $t('noticeManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('noticeManage.delete.cancelText') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('noticeManage.delete.confirmButton') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteNotice } from '@/api/oa/noticeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteNotice',
  data() {
    return {
      visible: false,
      currentNotice: null,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(notice) {
      this.currentNotice = notice
      this.visible = true
    },
    async handleConfirm() {
      try {
        const params = {
          ...this.currentNotice,
          communityId: this.communityId
        }
        await deleteNotice(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('noticeManage.delete.errorMessage'))
      }
    },
    handleClose() {
      this.currentNotice = null
    }
  }
}
</script>