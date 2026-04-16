<template>
  <el-dialog :title="$t('addAttendanceClasses.title')" :visible.sync="visible" width="60%" @close="closeDialog">
    <el-form ref="form" :model="formData" :rules="rules" label-width="150px" label-position="right">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('addAttendanceClasses.classesName')" prop="classesName">
            <el-input v-model="formData.classesName" :placeholder="$t('addAttendanceClasses.classesNamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addAttendanceClasses.timeOffset')" prop="timeOffset">
            <el-input v-model="formData.timeOffset" type="number"
              :placeholder="$t('addAttendanceClasses.timeOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('addAttendanceClasses.lateOffset')" prop="lateOffset">
            <el-input v-model="formData.lateOffset" type="number"
              :placeholder="$t('addAttendanceClasses.lateOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addAttendanceClasses.leaveOffset')" prop="leaveOffset">
            <el-input v-model="formData.leaveOffset" type="number"
              :placeholder="$t('addAttendanceClasses.leaveOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('addAttendanceClasses.maxLastOffset')" prop="maxLastOffset">
            <el-input v-model="formData.maxLastOffset" type="number"
              :placeholder="$t('addAttendanceClasses.maxLastOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveAttendanceClassesInfo">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveAttendanceClasses } from '@/api/oa/attendanceClassesManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddAttendanceClasses',
  data() {
    return {
      visible: false,
      formData: {
        classesId: '',
        classesName: '',
        timeOffset: '',
        leaveOffset: '',
        lateOffset: '',
        classesObjType: '',
        classesObjId: '',
        classesObjName: '',
        maxLastOffset: '',
        communityId: ''
      },
      rules: {
        classesName: [
          { required: true, message: this.$t('addAttendanceClasses.classesNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('addAttendanceClasses.classesNameMaxLength'), trigger: 'blur' }
        ],
        timeOffset: [
          { required: true, message: this.$t('addAttendanceClasses.timeOffsetRequired'), trigger: 'blur' },
        ],
        lateOffset: [
          { required: true, message: this.$t('addAttendanceClasses.lateOffsetRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('addAttendanceClasses.lateOffsetMaxLength'), trigger: 'blur' }
        ],
        leaveOffset: [
          { required: true, message: this.$t('addAttendanceClasses.leaveOffsetRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('addAttendanceClasses.leaveOffsetMaxLength'), trigger: 'blur' }
        ],
        maxLastOffset: [
          { required: true, message: this.$t('addAttendanceClasses.maxLastOffsetRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.formData = {
        ...this.formData,
        ...params,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    closeDialog() {
      this.visible = false
      this.$refs.form.resetFields()
    },
    async saveAttendanceClassesInfo() {
      try {
        await this.$refs.form.validate()
        const response = await saveAttendanceClasses(this.formData)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.closeDialog()
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('保存失败:', error)
      }
    }
  }
}
</script>