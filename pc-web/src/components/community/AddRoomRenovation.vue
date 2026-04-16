<template>
  <el-dialog :title="$t('roomRenovationManage.add')" :visible.sync="visible" width="40%" @close="resetForm">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('roomRenovationManage.room')" prop="roomName" required>
        <el-input v-model.trim="form.roomName" :placeholder="$t('roomRenovationManage.roomNumberPlaceholder')"
          @blur="queryRoom" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.contactPerson')" prop="personName" required>
        <el-input v-model.trim="form.personName" :placeholder="$t('roomRenovationManage.contactPersonPlaceholder')"
          disabled />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.phone')" prop="personTel" required>
        <el-input v-model.trim="form.personTel" :placeholder="$t('roomRenovationManage.phonePlaceholder')" disabled />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationStartTime')" prop="startTime" required>
        <el-date-picker v-model="form.startTime" type="date"
          :placeholder="$t('roomRenovationManage.startTimePlaceholder')" value-format="yyyy-MM-dd" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationEndTime')" prop="endTime" required>
        <el-date-picker v-model="form.endTime" type="date" :placeholder="$t('roomRenovationManage.endTimePlaceholder')"
          value-format="yyyy-MM-dd" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationCompany')" prop="renovationCompany" required>
        <el-input v-model.trim="form.renovationCompany"
          :placeholder="$t('roomRenovationManage.renovationCompanyPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationManager')" prop="personMain" required>
        <el-input v-model.trim="form.personMain"
          :placeholder="$t('roomRenovationManage.renovationManagerPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.managerPhone')" prop="personMainTel" required>
        <el-input v-model.trim="form.personMainTel" :placeholder="$t('roomRenovationManage.managerPhonePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.remark')" prop="remark">
        <el-input v-model.trim="form.remark" type="textarea"
          :placeholder="$t('roomRenovationManage.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('roomRenovationManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveRoomRenovation">
        {{ $t('roomRenovationManage.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRoomRenovation } from '@/api/community/roomRenovationManageApi'
import { queryRooms } from '@/api/room/roomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddRoomRenovation',
  data() {
    return {
      visible: false,
      form: {
        roomName: '',
        personName: '',
        personTel: '',
        startTime: '',
        endTime: '',
        renovationCompany: '',
        personMain: '',
        personMainTel: '',
        remark: '',
        isPostpone: 'N',
        communityId: ''
      },
      rules: {
        roomName: [
          { required: true, message: this.$t('roomRenovationManage.roomNumberPlaceholder'), trigger: 'blur' }
        ],
        personName: [
          { required: true, message: this.$t('roomRenovationManage.contactPersonPlaceholder'), trigger: 'blur' }
        ],
        personTel: [
          { required: true, message: this.$t('roomRenovationManage.phonePlaceholder'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('roomRenovationManage.startTimePlaceholder'), trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: this.$t('roomRenovationManage.endTimePlaceholder'), trigger: 'blur' }
        ],
        renovationCompany: [
          { required: true, message: this.$t('roomRenovationManage.renovationCompanyPlaceholder'), trigger: 'blur' }
        ],
        personMain: [
          { required: true, message: this.$t('roomRenovationManage.renovationManagerPlaceholder'), trigger: 'blur' }
        ],
        personMainTel: [
          { required: true, message: this.$t('roomRenovationManage.managerPhonePlaceholder'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },

    resetForm() {
      this.form = {
        roomName: '',
        personName: '',
        personTel: '',
        startTime: '',
        endTime: '',
        renovationCompany: '',
        personMain: '',
        personMainTel: '',
        remark: '',
        isPostpone: 'N',
        communityId: getCommunityId()
      }
      this.$refs.form && this.$refs.form.resetFields()
    },

    async saveRoomRenovation() {
      try {
                // 表单验证
        const valid = await this.$refs.form.validate()
        if (!valid) return


        this.form.communityId = getCommunityId()
        await addRoomRenovation(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('添加装修信息失败:', error)
        this.$message.error(error.message || this.$t('common.operationFailed'))
      }
    },

    async queryRoom() {
      if (!this.form.roomName) return

      try {
        // 这里需要根据实际API实现查询房屋信息
        const { rooms } = await queryRooms({ roomNum: this.form.roomName, page: 1, row: 10,flag:1, communityId: getCommunityId() })
        if (rooms.length > 0) {
          this.form.personName = rooms[0].ownerName
          this.form.personTel = rooms[0].link
          this.form.roomId = rooms[0].roomId
        }

        // const roomInfo = await getRoomInfo({ roomName: this.form.roomName })
        // this.form.personName = roomInfo.ownerName
        // this.form.personTel = roomInfo.link
      } catch (error) {
        console.error('查询房屋信息失败:', error)
      }
    }
  }
}
</script>
<style scoped>
.el-date-editor {
  width: 100%;
}

.el-date-editor.el-input {
  width: 100%;
}

.el-form-item {
  width: 95%;
}

.el-form-item__error {
  display: none !important;
}
</style>