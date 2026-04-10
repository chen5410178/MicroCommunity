<template>
  <el-dialog 
    :title="$t('roomRenovationManage.confirmComplete')" 
    :visible.sync="visible"
    width="30%"
    @close="resetForm"
  >
    <div>
      <p>{{ $t('roomRenovationManage.confirmCompleteMsg') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('roomRenovationManage.cancelComplete') }}
      </el-button>
      <el-button type="primary" @click="roomRenovationCompleted">
        {{ $t('roomRenovationManage.confirmComplete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateRoomRenovationState } from '@/api/community/roomRenovationManageApi'

export default {
  name: 'RoomRenovationCompleted',
  data() {
    return {
      visible: false,
      form: {
        rId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        rId: row.rId,
        roomId: row.roomId,
        communityId: this.getCommunityId()
      }
      this.visible = true
    },
    
    resetForm() {
      this.form = {
        rId: '',
        roomId: '',
        communityId: ''
      }
    },
    
    async roomRenovationCompleted() {
      try {
        await updateRoomRenovationState(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('更新装修状态失败:', error)
        this.$message.error(error.message || this.$t('common.operationFailed'))
      }
    }
  }
}
</script>