<template>
  <el-dialog
    :title="$t('deleteRoomDecorationRecord.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="confirm-message">
      {{ $t('deleteRoomDecorationRecord.confirm') }}
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('deleteRoomDecorationRecord.cancel') }}
      </el-button>
      <el-button type="primary" @click="confirmDelete">
        {{ $t('deleteRoomDecorationRecord.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteRoomRenovationRecord } from '@/api/community/listRoomDecorationRecordApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteRoomDecorationRecord',
  data() {
    return {
      visible: false,
      record: {}
    }
  },
  methods: {
    open(record) {
      this.record = { ...record }
      this.visible = true
    },
    handleClose() {
      this.record = {}
    },
    async confirmDelete() {
      try {
        this.record.communityId = getCommunityId()
        await deleteRoomRenovationRecord(this.record)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('deleteRoomDecorationRecord.failed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.confirm-message {
  text-align: center;
  font-size: 16px;
  margin-bottom: 20px;
}
</style>