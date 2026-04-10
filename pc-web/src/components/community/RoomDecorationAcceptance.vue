<template>
  <el-dialog 
    :title="$t('roomRenovationManage.renovationAcceptance')" 
    :visible.sync="visible"
    width="40%"
    @close="resetForm"
  >
    <el-form :model="form" ref="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('roomRenovationManage.room')" prop="roomName">
        <el-input 
          v-model.trim="form.roomName" 
          disabled
        />
      </el-form-item>
      
      <el-form-item :label="$t('roomRenovationManage.status')" prop="state" >
        <el-select v-model="form.state">
          <el-option 
            :label="$t('roomRenovationManage.acceptanceSuccess')" 
            value="5000" 
          />
          <el-option 
            :label="$t('roomRenovationManage.acceptanceFailed')" 
            value="6000" 
          />
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('roomRenovationManage.acceptanceOpinion')" prop="remark" >
        <el-input 
          v-model.trim="form.remark" 
          type="textarea"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('roomRenovationManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveRoomDecorationAcceptance">
        {{ $t('roomRenovationManage.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveRoomRenovationDetail } from '@/api/community/roomRenovationManageApi'

export default {
  name: 'RoomDecorationAcceptance',
  data() {
    return {
      visible: false,
      form: {
        rId: '',
        roomName: '',
        roomId: '',
        state: '',
        remark: '',
        detailType: '1001',
        communityId: ''
      },
      rules: {
        state: [
          { required: true, message: this.$t('common.required'), trigger: 'change' }
        ],
        remark: [
          { required: true, message: this.$t('common.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        rId: row.rId,
        roomName: row.roomName,
        roomId: row.roomId,
        state: '',
        remark: '',
        detailType: '1001',
        communityId: this.getCommunityId()
      }
      this.visible = true
    },
    
    resetForm() {
      this.form = {
        rId: '',
        roomName: '',
        roomId: '',
        state: '',
        remark: '',
        detailType: '1001',
        communityId: this.getCommunityId()
      }
    },
    
    async saveRoomDecorationAcceptance() {
      try {
        await saveRoomRenovationDetail(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('保存验收信息失败:', error)
        this.$message.error(error.message || this.$t('common.operationFailed'))
      }
    }
  }
}
</script>