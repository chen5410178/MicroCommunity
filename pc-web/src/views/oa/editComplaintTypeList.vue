<template>
  <el-card class="edit-complaint-type-container">
    <div slot="header" class="clearfix">
      <span>{{ $t('editComplaintType.title') }}</span>
    </div>

    <el-form ref="form" :model="editComplaintTypeInfo" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editComplaintType.typeName')" prop="typeName">
            <el-input v-model="editComplaintTypeInfo.typeName"
              :placeholder="$t('editComplaintType.typeNamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editComplaintType.notifyWay')" prop="notifyWay">
            <el-select v-model="editComplaintTypeInfo.notifyWay"
              :placeholder="$t('editComplaintType.notifyWayPlaceholder')" style="width:100%">
              <el-option v-for="item in notifyWayOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editComplaintType.appraiseReply')" prop="appraiseReply">
            <el-select v-model="editComplaintTypeInfo.appraiseReply"
              :placeholder="$t('editComplaintType.appraiseReplyPlaceholder')" style="width:100%">
              <el-option v-for="item in appraiseReplyOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editComplaintType.remark')" prop="remark">
            <el-input v-model="editComplaintTypeInfo.remark" type="textarea"
              :placeholder="$t('editComplaintType.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('editComplaintType.selectStaff')">
            <select-staffs ref="selectStaffs"  @selectStaffs="handleStaffSelected"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24" class="text-right">
          <el-button type="warning" @click="handleBack">
            <i class="el-icon-close"></i>
            {{ $t('common.back') }}
          </el-button>
          <el-button type="primary" @click="handleSave">
            <i class="el-icon-check"></i>
            {{ $t('common.save') }}
          </el-button>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
</template>

<script>
import { updateComplaintType, getComplaintTypeDetail } from '@/api/oa/editComplaintTypeApi'
import SelectStaffs from '@/components/staff/selectStaffsDiv'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditComplaintTypeList',
  components: {
    SelectStaffs
  },
  data() {
    return {
      editComplaintTypeInfo: {
        typeCd: '',
        typeName: '',
        notifyWay: '',
        appraiseReply: '',
        remark: '',
        staffs: [],
        communityId: ''
      },
      notifyWayOptions: [
        { value: 'SMS', label: this.$t('editComplaintType.sms') },
        { value: 'WECHAT', label: this.$t('editComplaintType.wechat') },
        { value: 'WORK_LICENSE', label: this.$t('editComplaintType.workLicense') }
      ],
      appraiseReplyOptions: [
        { value: 'Y', label: this.$t('editComplaintType.autoReply') },
        { value: 'N', label: this.$t('editComplaintType.manualReply') }
      ]
    }
  },
  created() {
    this.editComplaintTypeInfo.communityId = getCommunityId()
    this.editComplaintTypeInfo.typeCd = this.$route.query.typeCd
    this.loadComplaintTypeDetail()
  },
  methods: {
    handleStaffSelected(staffs) {
      this.editComplaintTypeInfo.staffs = staffs
    },
    async loadComplaintTypeDetail() {
      try {
        const params = {
          typeCd: this.editComplaintTypeInfo.typeCd,
          communityId: this.editComplaintTypeInfo.communityId,
          page: 1,
          row: 1
        }
        const { data } = await getComplaintTypeDetail(params)
        if (data && data.length > 0) {
          this.editComplaintTypeInfo = { ...data[0] }
          let _staffs = []
          data[0].staffs.forEach(staff => {
            _staffs.push({ userId: staff.staffId, name: staff.staffName })
          });
          this.$refs.selectStaffs.setStaffs(_staffs || [])
        }
      } catch (error) {
        this.$message.error(this.$t('editComplaintType.loadError'))
      }
    },
    validateForm() {
      return this.$refs.form.validate()
    },
    async handleSave() {
      try {
        const isValid = await this.validateForm()
        if (!isValid) return

        this.editComplaintTypeInfo.staffs = this.$refs.selectStaffs.getSelectedStaffs()
        await updateComplaintType(this.editComplaintTypeInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('editComplaintType.saveError'))
      }
    },
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-complaint-type-container {
  margin: 20px;

  .text-right {
    text-align: right;
  }

  .el-form-item {
    margin-bottom: 22px;
  }
}
</style>