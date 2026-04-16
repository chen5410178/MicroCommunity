<template>
  <div class="edit-examine-staff-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('editExamineStaff.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_goBack">
            <i class="el-icon-close"></i>{{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="editExamineStaffInfo" label-width="120px" class="text-left">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editExamineStaff.staffName')">
              <el-input v-model="editExamineStaffInfo.staffName" disabled
                :placeholder="$t('editExamineStaff.staffNamePlaceholder')">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editExamineStaff.projects')">
              <el-checkbox-group v-model="editExamineStaffInfo.projectIds">
                <el-checkbox v-for="(item, index) in editExamineStaffInfo.allProjects" :key="index"
                  :label="item.projectId">
                  {{ item.name }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editExamineStaff.staffPhoto')">
              <upload-image-url ref="uploadImage" :image-count="1" @notifyUploadCoverImage="handleUploadImage">
              </upload-image-url>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editExamineStaff.post')">
              <el-input v-model="editExamineStaffInfo.post" :placeholder="$t('editExamineStaff.postPlaceholder')">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('editExamineStaff.introduction')">
              <el-input type="textarea" :rows="5" v-model="editExamineStaffInfo.introduction">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="footer-buttons">
        <el-button type="warning" @click="_goBack">
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
        <el-button type="primary" @click="_updateExamineStaff">
          <i class="el-icon-check"></i>{{ $t('common.submit') }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import { updateExamineStaff, listExamineProjects, listExamineStaffs } from '@/api/oa/editExamineStaffApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditExamineStaffList',
  components: {
    UploadImageUrl
  },
  data() {
    return {
      editExamineStaffInfo: {
        esId: '',
        staffName: '',
        staffId: '',
        post: '',
        introduction: '',
        headerImg: '',
        allProjects: [],
        projectIds: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.editExamineStaffInfo.esId = this.$route.query.esId
    this._listExamineProjects()
    this._listExamineStaffs()
  },
  methods: {
    handleUploadImage(images) {
      if (images.length > 0) {
        this.editExamineStaffInfo.headerImg = images[0]
      } else {
        this.editExamineStaffInfo.headerImg = ''
      }
    },
    async _updateExamineStaff() {
      try {
        if (!this.validateForm()) {
          return
        }

        this.editExamineStaffInfo.communityId = this.communityId
        const res = await updateExamineStaff(this.editExamineStaffInfo)

        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.updateError'))
      }
    },
    validateForm() {
      if (!this.editExamineStaffInfo.staffId) {
        this.$message.error(this.$t('editExamineStaff.staffNameRequired'))
        return false
      }
      if (!this.editExamineStaffInfo.post) {
        this.$message.error(this.$t('editExamineStaff.postRequired'))
        return false
      }
      return true
    },
    async _listExamineProjects() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listExamineProjects(params)
        this.editExamineStaffInfo.allProjects = data
      } catch (error) {
        this.$message.error(this.$t('editExamineStaff.fetchProjectsError'))
      }
    },
    async _listExamineStaffs() {
      try {
        const params = {
          page: 1,
          row: 1,
          esId: this.editExamineStaffInfo.esId,
          communityId: this.communityId
        }
        const { data } = await listExamineStaffs(params)

        if (data && data.length > 0) {
          Object.assign(this.editExamineStaffInfo, data[0])
          this.editExamineStaffInfo.projectIds = data[0].projects.map(item => item.projectId)

          if (this.editExamineStaffInfo.headerImg) {
            this.$refs.uploadImage.setImages([this.editExamineStaffInfo.headerImg])
          }
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error(this.$t('editExamineStaff.fetchStaffError'))
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-examine-staff-container {
  padding: 20px;

  .box-card {
    width: 100%;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .header-tools {
      float: right;
    }

    .footer-buttons {
      text-align: right;
      margin-top: 20px;

      .el-button {
        margin-left: 10px;
      }
    }
  }

  .el-form-item {
    margin-bottom: 22px;
  }

  .el-checkbox {
    margin-right: 15px;
  }
}
</style>