<template>
  <el-dialog :title="$t('room.deleteFloor.title')" :visible.sync="visible" width="30%" :before-close="handleClose">
    <p>{{ $t('room.deleteFloor.confirmMessage') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('room.deleteFloor.cancelText') }}</el-button>
      <el-button type="danger" @click="handleDelete">{{ $t('room.deleteFloor.confirmDelete') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteFloor } from '@/api/room/deleteFloorApi'

export default {
  name: 'DeleteFloor',
  data() {
    return {
      visible: false,
      floorInfo: {
        floorId: '',
        communityId: this.getCommunityId()
      }
    }
  },
  methods: {
    open(floorInfo) {
      this.floorInfo = {
        ...floorInfo,
        communityId: this.getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async handleDelete() {
      try {
        const response = await deleteFloor(this.floorInfo)

        if (response.code == 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleClose()
          this.$emit('handleRefreshTree', {})

        } else {
          this.$message.error(response.msg || this.$t('room.deleteFloor.deleteFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('room.deleteFloor.deleteError'))
      }
    },
  }
}
</script>