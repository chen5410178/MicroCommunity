<template>
  <el-dialog
    :title="$t('smallWeChatManage.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('smallWeChatManage.deleteConfirm') }}</p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteSmallWeChat } from '@/api/system/smallWeChatManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteSmallWechat',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        wechatId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.formData.wechatId = data.wechatId
      this.formData.communityId = getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.formData = {
        wechatId: '',
        communityId: ''
      }
      this.loading = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteSmallWeChat(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('smallWeChatManage.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>