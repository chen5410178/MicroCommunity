<template>
  <div class="add-attendance-classes-staff-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addAttendanceClassesStaff.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="handleGoBack">
          <i class="el-icon-close"></i>
          {{ $t('common.back') }}
        </el-button>
      </div>

      <el-row>
        <el-col :span="24">
          <el-form label-width="120px">
            <el-form-item :label="$t('addAttendanceClassesStaff.staffName')" class="text-left">
              <el-input v-model="form.staffName" style="width: 80%;" :placeholder="$t('addAttendanceClassesStaff.staffNamePlaceholder')"
                disabled></el-input>
              <el-button type="primary" @click="handleChooseStaff" class="ml-10">
                <i class="el-icon-search"></i>
                {{ $t('common.select') }}
              </el-button>
            </el-form-item>

            <el-form-item :label="$t('addAttendanceClassesStaff.attendanceFace')">
              <upload-image-url ref="uploadImage" :image-count="1"
              @notifyUploadCoverImage="handleImageUpload"></upload-image-url>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>

      <div class="text-right mt-20">
        <el-button type="warning" @click="handleGoBack" class="mr-20">
          {{ $t('common.back') }}
        </el-button>
        <el-button type="primary" @click="handleSubmit">
          <i class="el-icon-check"></i>
          {{ $t('common.submit') }}
        </el-button>
      </div>
    </el-card>

    <select-staff ref="selectStaff" @selectStaff="handleStaffChange"></select-staff>
  </div>
</template>

<script>
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import SelectStaff from '@/components/staff/SelectStaff'
import { saveAttendanceClassesStaff } from '@/api/oa/addAttendanceClassesStaffApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddAttendanceClassesStaffList',
  components: {
    UploadImageUrl,
    SelectStaff
  },
  data() {
    return {
      form: {
        csId: '',
        classesId: '',
        staffId: '',
        staffName: '',
        photo: '',
        communityId: ''
      }
    }
  },
  created() {
    this.form.classesId = this.$route.query.classesId
    this.form.communityId = getCommunityId()
  },
  methods: {
    handleChooseStaff() {
      this.$refs.selectStaff.open(this.form)
    },
    handleStaffChange(staff) {
      this.form.staffId = staff.staffId
      this.form.staffName = staff.staffName
    },
    handleImageUpload(photos) {
      if (photos.length > 0) {
        this.form.photo = photos[0]
      }
    },
    async handleSubmit() {
      if (!this.validateForm()) {
        return
      }

      try {
        const res = await saveAttendanceClassesStaff(this.form)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleGoBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.addFailed'))
      }
    },
    validateForm() {
      if (!this.form.staffName) {
        this.$message.error(this.$t('addAttendanceClassesStaff.staffNameRequired'))
        return false
      }
      return true
    },
    handleGoBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-attendance-classes-staff-container {
  padding: 20px;

  .el-form-item {
    margin-bottom: 22px;
  }

  .ml-10 {
    margin-left: 10px;
  }

  .mt-20 {
    margin-top: 20px;
  }

  .mr-20 {
    margin-right: 20px;
  }

  .text-right {
    text-align: right;
  }

  .float-right {
    float: right;
  }
}
</style>