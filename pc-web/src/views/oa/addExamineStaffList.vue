<template>
  <div class="add-examine-staff-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addExamineStaff.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>
      <el-form ref="form" :model="addExamineStaffInfo" label-width="120px" class="text-left">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addExamineStaff.staffName')">
              <el-input v-model="addExamineStaffInfo.staffName" :placeholder="$t('addExamineStaff.staffNamePlaceholder')"
                disabled style="width: 60%">
              </el-input>
              <el-button type="primary" @click="chooseStaff" style="margin-left: 10px">
                <i class="el-icon-search"></i>{{ $t('addExamineStaff.select') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addExamineStaff.projects')">
              <el-checkbox-group v-model="addExamineStaffInfo.projectIds">
                <el-checkbox v-for="(item, index) in addExamineStaffInfo.projects" :key="index" :label="item.projectId">
                  {{ item.name }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addExamineStaff.staffAvatar')">
              <upload-image-url ref="uploadImage" @notifyUploadCoverImage="notifyUploadImage" :image-count="1">
              </upload-image-url>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addExamineStaff.staffPost')">
              <el-input v-model="addExamineStaffInfo.post" :placeholder="$t('addExamineStaff.staffPostPlaceholder')"
                style="width: 60%">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addExamineStaff.staffIntroduction')">
              <el-input type="textarea" :rows="5" v-model="addExamineStaffInfo.introduction"
                :placeholder="$t('addExamineStaff.staffIntroductionPlaceholder')" style="width: 80%">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" class="text-right">
            <el-button type="warning" @click="_goBack" style="margin-right: 20px;">
              <i class="el-icon-close"></i>{{ $t('common.back') }}
            </el-button>
            <el-button type="primary" @click="saveExamineStaffInfo">
              <i class="el-icon-check"></i>{{ $t('common.submit') }}
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <select-staff ref="selectStaff" @selectStaff="handleStaffSelected"></select-staff>
  </div>
</template>

<script>
import { saveExamineStaff, listExamineProject } from '@/api/oa/addExamineStaffApi'
import SelectStaff from '@/components/staff/SelectStaff'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddExamineStaffList',
  components: {
    SelectStaff,
    UploadImageUrl
  },
  data() {
    return {
      addExamineStaffInfo: {
        esId: '',
        orgName: '',
        staffName: '',
        staffId: '',
        post: '',
        introduction: '',
        headerImg: '',
        projects: [],
        projectIds: [],
        communityId: ''
      }
    }
  },
  created() {
    this.addExamineStaffInfo.communityId = getCommunityId()
    this._listExamineProjects()
  },
  methods: {
    notifyUploadImage(_param) {
      if (_param.length > 0) {
        this.addExamineStaffInfo.headerImg = _param[0]
      } else {
        this.addExamineStaffInfo.headerImg = ''
      }
    },
    handleStaffSelected(staffs) {
      console.log(staffs)
      this.addExamineStaffInfo.staffName = staffs.staffName
      this.addExamineStaffInfo.staffId = staffs.staffId
    },
    addExamineStaffValidate() {
      return this.$refs.form.validate()
    },
    async saveExamineStaffInfo() {
      try {
        const valid = await this.addExamineStaffValidate()
        if (!valid) return

        const res = await saveExamineStaff(this.addExamineStaffInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.addFailed'))
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.addExamineStaffInfo)
    },
    async _listExamineProjects() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.addExamineStaffInfo.communityId
        }
        const res = await listExamineProject(params)
        this.addExamineStaffInfo.projects = res.data
      } catch (error) {
        console.error('获取考核项目失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-examine-staff-container {
  padding: 20px;

  .box-card {
    width: 100%;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .header-tools {
      display: flex;
      align-items: center;
    }

    .text-right {
      text-align: right;
      padding-right: 20px;
    }
  }

  .el-checkbox {
    margin-right: 15px;
  }
}
</style>