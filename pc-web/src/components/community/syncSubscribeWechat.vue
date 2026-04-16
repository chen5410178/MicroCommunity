<template>
  <el-dialog
    :title="$t('wechatSubscribe.confirmTitle')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose">
    <div class="dialog-content">
      <p>{{ $t('wechatSubscribe.syncConfirmText') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="syncSubscribeWechat">{{ $t('common.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { synchronizeWechatSubscribe } from '@/api/community/wechatSubscribeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SyncSubscribeWechat',
  data() {
    return {
      visible: false
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async syncSubscribeWechat() {
      try {
        const params = {
          communityId: getCommunityId()
        }
        await synchronizeWechatSubscribe(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('wechatSubscribe.syncError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-content {
  padding: 20px;
  text-align: center;
  
  p {
    margin: 0;
    line-height: 1.5;
  }
}

.dialog-footer {
  text-align: right;
}
</style>