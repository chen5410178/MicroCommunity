<template>
  <el-dialog 
    :title="$t('editParkingSpaceApply.editApplication')" 
    :visible.sync="visible" 
    width="800px"
    @close="closeDialog"
  >
    <el-form 
      ref="form" 
      :model="formData" 
      :rules="rules" 
      label-width="150px"
    >
      <el-form-item 
        :label="$t('editParkingSpaceApply.licensePlate')" 
        prop="carNum"
      >
        <el-input 
          v-model="formData.carNum" 
          :placeholder="$t('editParkingSpaceApply.fillLicensePlate')"
        />
      </el-form-item>
      
      <el-form-item 
        :label="$t('editParkingSpaceApply.vehicleType')" 
        prop="carType"
      >
        <el-select 
          v-model="formData.carType" 
          :placeholder="$t('editParkingSpaceApply.selectVehicleType')"
          style="width: 100%"
        >
          <el-option 
            :label="$t('parkingSpaceApplyManage.homeCar')" 
            value="9901" 
          />
          <el-option 
            :label="$t('parkingSpaceApplyManage.bus')" 
            value="9902" 
          />
          <el-option 
            :label="$t('parkingSpaceApplyManage.truck')" 
            value="9903" 
          />
        </el-select>
      </el-form-item>
      
      <el-form-item 
        :label="$t('editParkingSpaceApply.startTime')" 
        prop="startTime"
      >
        <el-date-picker
          v-model="formData.startTime"
          type="datetime"
          :placeholder="$t('editParkingSpaceApply.fillStartTime')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item 
        :label="$t('editParkingSpaceApply.endTime')" 
        prop="endTime"
      >
        <el-date-picker
          v-model="formData.endTime"
          type="datetime"
          :placeholder="$t('editParkingSpaceApply.fillEndTime')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpaceApply.applicant')">
        <el-input 
          v-model="formData.applyPersonName" 
          disabled 
          :placeholder="$t('editParkingSpaceApply.fillApplicant')"
        />
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpaceApply.applicantPhone')">
        <el-input 
          v-model="formData.applyPersonLink" 
          disabled 
          :placeholder="$t('editParkingSpaceApply.fillApplicantPhone')"
        />
      </el-form-item>
      
      <el-form-item :label="$t('editParkingSpaceApply.remarks')">
        <el-input 
          v-model="formData.remark" 
          type="textarea" 
          :rows="3"
          :placeholder="$t('editParkingSpaceApply.optional')"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('editParkingSpaceApply.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('editParkingSpaceApply.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateParkingSpaceApply } from '@/api/car/parkingSpaceApplyManageApi'

export default {
  name: 'EditParkingSpaceApply',
  data() {
    return {
      visible: false,
      formData: {
        applyId: '',
        carNum: '',
        carType: '',
        startTime: '',
        endTime: '',
        applyPersonName: '',
        applyPersonLink: '',
        applyPersonId: '',
        state: '1001',
        remark: ''
      },
      rules: {
        carNum: [
          { required: true, message: this.$t('editParkingSpaceApply.fillLicensePlate'), trigger: 'blur' }
        ],
        carType: [
          { required: true, message: this.$t('editParkingSpaceApply.selectVehicleType'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('editParkingSpaceApply.fillStartTime'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('editParkingSpaceApply.fillEndTime'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.formData = {
        applyId: data.applyId,
        carNum: data.carNum,
        carType: data.carType,
        startTime: data.startTime,
        endTime: data.endTime,
        applyPersonName: data.applyPersonName,
        applyPersonLink: data.applyPersonLink,
        applyPersonId: data.applyPersonId,
        state: data.state || '1001',
        remark: data.remark || ''
      }
      this.visible = true
    },
    
    closeDialog() {
      this.$refs.form.resetFields()
    },
    
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const response = await updateParkingSpaceApply(this.formData)
            if (response.code === 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.visible = false
              this.$emit('success')
            } else {
              this.$message.error(response.msg || this.$t('common.updateFailed'))
            }
          } catch (error) {
            console.error('Error updating parking space application:', error)
            this.$message.error(this.$t('common.requestError'))
          }
        }
      })
    }
  }
}
</script>