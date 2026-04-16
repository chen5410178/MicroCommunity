<template>
  <el-dialog :title="$t('common.confirm')" :visible.sync="visible" width="500px" center @close="close">
    <div class="modal-body">
      <div class="text-center">
        <p>{{ $t('common.confirmExit') }}</p>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="doOwnerExitRoom">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { exitRoom } from '@/api/owner/showOwnerRoomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerExitRoom',
  data() {
    return {
      visible: false,
      roomInfo: {}
    }
  },
  methods: {
    open(roomInfo) {
      this.roomInfo = roomInfo
      this.visible = true
    },
    close() {
      this.visible = false
      this.roomInfo = {}
    },
    async doOwnerExitRoom() {
      try {
        const params = {
          ...this.roomInfo,
          communityId: getCommunityId()
        }

        const res = await exitRoom(params)
        if (res.code === 0) {
          this.$emit('success')
          this.close()
          this.$message.success('退房成功')
        } else {
          this.$message.error(res.msg || '退房失败')
        }
      } catch (error) {
        this.$message.error('请求失败')
      }
    }
  }
}
</script>

<style scoped>
.modal-body {
  padding: 20px;
  text-align: center;
}

.text-center {
  text-align: center;
}

.dialog-footer {
  text-align: center;
}
</style>