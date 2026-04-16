<template>
  <el-dialog 
    :title="$t('roomRenovationManage.confirmDelete')" 
    :visible.sync="visible"
    width="30%"
    @close="resetForm"
  >
    <div>
      <p>{{ $t('roomRenovationManage.confirmDeleteMsg') }}</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('roomRenovationManage.cancelDelete') }}
      </el-button>
      <el-button type="danger" @click="deleteRoomRenovation">
        {{ $t('roomRenovationManage.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteRoomRenovation } from '@/api/community/roomRenovationManageApi'

export default {
  name: 'DeleteRoomRenovation',
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
        communityId: this.getCommunityId()
      }
      this.visible = true
    },
    
    resetForm() {
      this.form = {
        rId: '',
        communityId: ''
      }
    },
    
    async deleteRoomRenovation() {
      try {
        await deleteRoomRenovation(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除装修信息失败:', error)
        this.$message.error(error.message || this.$t('common.operationFailed'))
      }
    }
  }
}
</script>