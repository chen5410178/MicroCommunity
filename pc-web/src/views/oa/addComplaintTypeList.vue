<template>
  <div class="padding">
    <el-card class="complaint-type-container">
      <div class="header-wrapper">
        <h3>{{ $t('addComplaintType.title') }}</h3>
      </div>

      <el-form ref="form" :model="addComplaintTypeInfo" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addComplaintType.typeName')" prop="typeName">
              <el-input v-model="addComplaintTypeInfo.typeName"
                :placeholder="$t('addComplaintType.typeNamePlaceholder')" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addComplaintType.notifyWay')" prop="notifyWay">
              <el-select v-model="addComplaintTypeInfo.notifyWay"
                :placeholder="$t('addComplaintType.notifyWayPlaceholder')" style="width:100%">
                <el-option v-for="item in notifyWayOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('addComplaintType.appraiseReply')" prop="appraiseReply">
              <el-select v-model="addComplaintTypeInfo.appraiseReply"
                :placeholder="$t('addComplaintType.appraiseReplyPlaceholder')" style="width:100%">
                <el-option v-for="item in appraiseReplyOptions" :key="item.value" :label="item.label"
                  :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('addComplaintType.remark')" prop="remark">
              <el-input v-model="addComplaintTypeInfo.remark" type="textarea"
                :placeholder="$t('addComplaintType.remarkPlaceholder')" :rows="2" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addComplaintType.selectStaff')">
              <select-staffs ref="selectStaffs" @selectStaffs="handleStaffSelected" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="button-group">
            <el-button type="primary" @click="saveComplaintTypeInfo">
              {{ $t('common.save') }}
            </el-button>
            <el-button @click="goBack">
              {{ $t('common.back') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import SelectStaffs from '@/components/staff/selectStaffsDiv'
import { saveComplaintType } from '@/api/oa/addComplaintTypeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddComplaintTypeList',
  components: {
    SelectStaffs
  },
  data() {
    return {
      addComplaintTypeInfo: {
        typeName: '',
        notifyWay: '',
        appraiseReply: '',
        remark: '',
        staffs: []
      },
      notifyWayOptions: [
        { value: 'SMS', label: this.$t('addComplaintType.sms') },
        { value: 'WECHAT', label: this.$t('addComplaintType.wechat') },
        { value: 'WORK_LICENSE', label: this.$t('addComplaintType.workLicense') }
      ],
      appraiseReplyOptions: [
        { value: 'Y', label: this.$t('addComplaintType.autoReply') },
        { value: 'N', label: this.$t('addComplaintType.manualReply') }
      ]
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    validateForm() {
      return this.$refs.form.validate()
    },
    handleStaffSelected(staffs) {
      this.addComplaintTypeInfo.staffs = staffs
    },
    async saveComplaintTypeInfo() {
      try {
        const isValid = await this.validateForm()
        if (!isValid) return

        // 获取选择的员工
        this.addComplaintTypeInfo.staffs = this.$refs.selectStaffs.getSelectedStaffs()

        const params = {
          ...this.addComplaintTypeInfo,
          communityId: this.communityId
        }

        const res = await saveComplaintType(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('保存投诉类型失败:', error)
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.complaint-type-container {
  .header-wrapper {
    margin-bottom: 20px;

    h3 {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .button-group {
    text-align: right;
    margin-top: 20px;

    .el-button {
      margin-left: 10px;
    }
  }
}
</style>