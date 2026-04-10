<template>
  <div class="add-complaint-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addComplaint.title') }}</span>
      </div>
      <el-form ref="form" :model="addComplaintInfo" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addComplaint.type')" prop="typeCd">
              <el-select v-model="addComplaintInfo.typeCd" :placeholder="$t('addComplaint.typePlaceholder')"
                style="width:100%">
                <el-option v-for="item in addComplaintInfo.complaintTypes" :key="item.typeCd" :label="item.typeName"
                  :value="item.typeCd" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addComplaint.room')" class="text-left" prop="roomName">
              <el-input v-model="addComplaintInfo.roomName" style="width: 80%;" :placeholder="$t('addComplaint.roomPlaceholder')" disabled />
              <el-button type="primary" @click="selectComplaintRoom" style="margin-left:10px">
                {{ $t('addComplaint.register') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addComplaint.contact')" prop="complaintName">
              <el-input v-model="addComplaintInfo.complaintName" :placeholder="$t('addComplaint.contactPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addComplaint.phone')" prop="tel">
              <el-input v-model="addComplaintInfo.tel" :placeholder="$t('addComplaint.phonePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addComplaint.content')" prop="context">
              <el-input type="textarea" :rows="4" v-model="addComplaintInfo.context"
                :placeholder="$t('addComplaint.contentPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24" style="text-align:right">
            <el-button type="warning" @click="goBack">{{ $t('common.back') }}</el-button>
            <el-button type="primary" @click="saveComplaint">{{ $t('addComplaint.register') }}</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <room-tree ref="roomTree" @selectRoom="selectRoom" />

  </div>
</template>

<script>
import { listComplaintTypes, saveComplaint } from '@/api/oa/addComplaintApi'
import { getCommunityId } from '@/api/community/communityApi'
import roomTree from '@/components/room/roomTree'

export default {
  name: 'AddComplaintList',
  data() {
    return {
      addComplaintInfo: {
        complaintTypes: [],
        typeCd: '',
        complaintName: '',
        tel: '',
        context: '',
        communityId: '',
        roomId: '',
        roomName: '',
        ownerId: '',
        ownerName: ''
      },
      rules: {
        typeCd: [{ required: true, message: this.$t('addComplaint.typeRequired'), trigger: 'blur' }],
        complaintName: [
          { required: true, message: this.$t('addComplaint.contactRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('addComplaint.contactMaxLength'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('addComplaint.phoneRequired'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('addComplaint.phoneFormat'), trigger: 'blur' }
        ],
        context: [
          { required: true, message: this.$t('addComplaint.contentRequired'), trigger: 'blur' },
          { max: 4000, message: this.$t('addComplaint.contentMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  components: { roomTree },
  created() {
    this.communityId = getCommunityId()
    const { roomId,roomName } = this.$route.query
    if (roomId) {
      this.addComplaintInfo.roomId = roomId
      this.addComplaintInfo.roomName = roomName
    }
    this.listComplaintTypes()
  },
  methods: {
    async listComplaintTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listComplaintTypes(params)
        this.addComplaintInfo.complaintTypes = data
      } catch (error) {
        this.$message.error(this.$t('addComplaint.fetchTypesError'))
      }
    },
    selectComplaintRoom() {
      this.$refs.roomTree.open()
    },
    selectRoom(room) {
      this.addComplaintInfo.roomId = room.roomId
      this.addComplaintInfo.roomName = room.roomName
    },
    async saveComplaint() {
      await this.$refs.form.validate()

      try {
        this.addComplaintInfo.communityId = this.communityId
        await saveComplaint(this.addComplaintInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('addComplaint.saveError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.add-complaint-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}
</style>