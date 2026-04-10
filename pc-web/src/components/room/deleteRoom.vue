<template>
  <el-dialog :title="$t('room.deleteRoom.title')" :visible.sync="dialogVisible" width="30%">
    <div style="text-align: center">
      <p>{{ $t('room.deleteRoom.confirmMessage') }}</p>
    </div>
    <div slot="footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="deleteRoom">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteRoom } from '@/api/room/deleteRoomApi'

export default {
  name: 'DeleteRoom',
  data() {
    return {
      dialogVisible: false,
      roomInfo: {}
    }
  },
  created() {
  },
  beforeDestroy() {
  },
  methods: {
    open(roomInfo) {
      this.roomInfo = roomInfo
      this.dialogVisible = true
    },
    async deleteRoom() {
      try {
        this.roomInfo.communityId = this.getCommunityId()

        await deleteRoom(this.roomInfo)

        this.dialogVisible = false
        this.$emit('handleRefreshRoom')

        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error('删除房屋失败', error)
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    },
  }
}
</script>