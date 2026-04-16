<template>
  <el-dialog :title="$t('editAttendanceClasses.title')" :visible.sync="visible" width="60%" @close="closeDialog">
    <el-form ref="form" :model="formData" :rules="rules" label-width="150px" label-position="right">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('editAttendanceClasses.classesName')" prop="classesName">
            <el-input v-model="formData.classesName"
              :placeholder="$t('editAttendanceClasses.classesNamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editAttendanceClasses.timeOffset')" prop="timeOffset">
            <el-input v-model="formData.timeOffset" type="number"
              :placeholder="$t('editAttendanceClasses.timeOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('editAttendanceClasses.lateOffset')" prop="lateOffset">
            <el-input v-model="formData.lateOffset" type="number"
              :placeholder="$t('editAttendanceClasses.lateOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editAttendanceClasses.leaveOffset')" prop="leaveOffset">
            <el-input v-model="formData.leaveOffset" type="number"
              :placeholder="$t('editAttendanceClasses.leaveOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('editAttendanceClasses.maxLastOffset')" prop="maxLastOffset">
            <el-input v-model="formData.maxLastOffset" type="number"
              :placeholder="$t('editAttendanceClasses.maxLastOffsetPlaceholder')">
              <template slot="append">{{ $t('attendanceClassesManage.minute') }}</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="editAttendanceClasses">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateAttendanceClasses } from '@/api/oa/attendanceClassesManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditAttendanceClasses',
  data() {
    return {
      visible: false,
      formData: {
        classesId: '',
        classesName: '',
        timeOffset: '',
        clockCount: '',
        clockTypes: [],
        clockType: '',
        clockTypeValue: '',
        leaveOffset: '',
        lateOffset: '',
        maxLastOffset: '',
        attrs: [],
        clockTypeValues: [],
        communityId: ''
      },
      rules: {
        classesName: [
          { required: true, message: this.$t('editAttendanceClasses.classesNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('editAttendanceClasses.classesNameMaxLength'), trigger: 'blur' }
        ],
        timeOffset: [
          { required: true, message: this.$t('editAttendanceClasses.timeOffsetRequired'), trigger: 'blur' },
        ],
        lateOffset: [
          { required: true, message: this.$t('editAttendanceClasses.lateOffsetRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('editAttendanceClasses.lateOffsetMaxLength'), trigger: 'blur' }
        ],
        leaveOffset: [
          { required: true, message: this.$t('editAttendanceClasses.leaveOffsetRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('editAttendanceClasses.leaveOffsetMaxLength'), trigger: 'blur' }
        ],
        maxLastOffset: [
          { required: true, message: this.$t('editAttendanceClasses.maxLastOffsetRequired'), trigger: 'blur' }
        ],
        classesId: [
          { required: true, message: this.$t('editAttendanceClasses.classesIdRequired'), trigger: 'blur' }
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
      this.getClockTypes()
      this.visible = true
    },
    closeDialog() {
      this.visible = false
      this.$refs.form.resetFields()
    },
    async getClockTypes() {
      try {
        const data = await getDict('attendance_classes', 'clock_type')
        this.formData.clockTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async editAttendanceClasses() {
      try {
        await this.$refs.form.validate()
        const response = await updateAttendanceClasses(this.formData)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.closeDialog()
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('修改失败:', error)
      }
    }
  }
}
</script>