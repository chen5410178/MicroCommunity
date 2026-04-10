<template>
  <el-dialog :title="$t('roomRenovationManage.review')" :visible.sync="visible" width="40%" @close="resetForm">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('roomRenovationManage.room')" prop="roomName">
        <el-input v-model.trim="form.roomName" disabled />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.status')" prop="state">
        <el-select v-model="form.state">
          <el-option :label="$t('roomRenovationManage.reviewPass')" value="3000" />
          <el-option :label="$t('roomRenovationManage.reviewReject')" value="2000" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.reviewOpinion')" prop="examineRemark">
        <el-input v-model.trim="form.examineRemark" type="textarea" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('roomRenovationManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveRoomToExamine">
        {{ $t('roomRenovationManage.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateRoomToExamine } from '@/api/community/roomRenovationManageApi'

export default {
  name: 'RoomToExamine',
  data() {
    return {
      visible: false,
      form: {
        rId: '',
        roomName: '',
        state: '',
        examineRemark: '',
        communityId: ''
      },
      rules: {
        state: [
          { required: true, message: this.$t('common.pleaseSelect'), trigger: 'change' }
        ],
        examineRemark: [
          { required: true, message: this.$t('common.pleaseInput'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        rId: row.rId,
        roomId: row.roomId,
        roomName: row.roomName,
        state: '',
        examineRemark: '',
        communityId: this.getCommunityId()
      }
      this.visible = true
    },

    resetForm() {
      this.form = {
        rId: '',
        roomId: '',
        roomName: '',
        state: '',
        examineRemark: '',
        communityId: ''
      }
      this.$refs.form && this.$refs.form.resetFields()
    },

    async saveRoomToExamine() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) {
          return false
        }
        try {
          await updateRoomToExamine(this.form)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          console.error('保存审核信息失败:', error)
          this.$message.error(error.message || this.$t('common.operationFailed'))
        }
      })
    }
  }
}
</script>