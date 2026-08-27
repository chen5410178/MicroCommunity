<template>
  <el-dialog 
    :title="$t('listOwnerCar.editCar')" 
    :visible.sync="visible" 
    width="800px"
    @close="resetForm">
    <el-form :model="form" label-width="110px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.carNum')" required>
            <el-input 
              v-model="form.carNum" 
              :placeholder="$t('listOwnerCar.inputCarNumRequired')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.carType')" required>
            <el-select 
              v-model="form.carType" 
              :placeholder="$t('listOwnerCar.selectType')"
              style="width: 100%">
              <el-option 
                v-for="item in carTypes" 
                :key="item.statusCd" 
                :label="item.name" 
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.contact')">
            <el-input 
              v-model="form.contact" 
              :placeholder="$t('listOwnerCar.inputContact')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.tel')">
            <el-input 
              v-model="form.tel" 
              :placeholder="$t('listOwnerCar.inputTel')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.licensePlateType')" required>
            <el-select 
              v-model="form.leaseType" 
              :placeholder="$t('listOwnerCar.selectLicensePlateType')"
              style="width: 100%">
              <el-option value="H" :label="$t('listOwnerCar.monthlyRentCar')"></el-option>
              <el-option value="I" :label="$t('listOwnerCar.internalCar')"></el-option>
              <el-option value="NM" :label="$t('listOwnerCar.freeCar')"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.parkingArea')">
            <el-select v-model="form.paId" style="width: 100%" clearable
              :placeholder="$t('listOwnerCar.selectParkingArea')">
              <el-option v-for="item in parkingAreas" :key="item.paId" :label="item.num" :value="item.paId" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="form.leaseType === 'H'" :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.startRentTime')" required>
            <el-date-picker
              v-model="form.startTime"
              type="datetime"
              :placeholder="$t('listOwnerCar.inputStartRentTime')"
              style="width: 100%"
              value-format="yyyy-MM-dd HH:mm:ss" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('listOwnerCar.endRentTime')" required>
            <el-date-picker
              v-model="form.endTime"
              type="datetime"
              :placeholder="$t('listOwnerCar.inputEndRentTime')"
              style="width: 100%"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabledDate="disabledEndDate" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('listOwnerCar.remark')">
            <el-input 
              v-model="form.remark" 
              type="textarea" 
              :rows="3" 
              :placeholder="$t('listOwnerCar.inputRemark')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm" :loading="loading">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { editOwnerCar } from '@/api/car/listOwnerCarApi'
import { getDict } from '@/api/community/communityApi'
import { listParkingAreas } from '@/api/car/hireParkingSpaceApi'

export default {
  name: 'EditCar',
  data() {
    return {
      visible: false,
      loading: false,
      carTypes: [],
      parkingAreas: [],
      form: {
        carId: '',
        memberId: '',
        carNum: '',
        carType: '',
        contact: '',
        tel: '',
        remark: '',
        startTime: '',
        endTime: '',
        leaseType: '',
        paId: ''
      }
    }
  },
  mounted() {
    this.getCarTypes()
  },
  methods: {
    async open(carInfo) {
      this.form = { ...carInfo }
      this.visible = true
      await this.getParkingAreas()
    },

    async getParkingAreas() {
      try {
        const res = await listParkingAreas({ page: 1, row: 50 })
        this.parkingAreas = res.data || res.parkingAreas || []
      } catch (error) {
        console.error('加载停车场列表失败:', error)
      }
    },
    
    async getCarTypes() {
      try {
        this.carTypes = await getDict('owner_car', 'car_type')
      } catch (error) {
        console.error('获取车辆类型失败:', error)
      }
    },
    
    disabledEndDate(endTime) {
      if (!this.form.startTime) return false
      const startTime = new Date(this.form.startTime)
      return endTime.getTime() <= startTime.getTime()
    },
    
    async submitForm() {
      if (!this.validateForm()) return
      
      this.loading = true
      try {
        await editOwnerCar(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('更新车辆信息失败:', error)
        this.$message.error(error.message || this.$t('listOwnerCar.updateError'))
      } finally {
        this.loading = false
      }
    },
    
    validateForm() {
      if (!this.form.carNum) {
        this.$message.warning(this.$t('listOwnerCar.inputCarNumRequired'))
        return false
      }
      
      if (!this.form.carType) {
        this.$message.warning(this.$t('listOwnerCar.selectTypeRequired'))
        return false
      }
      
      if (!this.form.leaseType) {
        this.$message.warning(this.$t('listOwnerCar.selectLicensePlateTypeRequired'))
        return false
      }
      
      if ((this.form.leaseType === 'H') && 
          (!this.form.startTime || !this.form.endTime)) {
        this.$message.warning(this.$t('listOwnerCar.rentTimeRequired'))
        return false
      }
      
      if (this.form.tel && !/^1[3-9]\d{9}$/.test(this.form.tel)) {
        this.$message.warning(this.$t('listOwnerCar.telFormat'))
        return false
      }

      if (this.form.startTime && this.form.endTime) {
        const start = new Date(this.form.startTime)
        const end = new Date(this.form.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('listOwnerCar.endTimeAfterStart'))
          return false
        }
      }
      
      return true
    },
    
    resetForm() {
      this.form = {
        carId: '',
        memberId: '',
        carNum: '',
        carType: '',
        contact: '',
        tel: '',
        remark: '',
        startTime: '',
        endTime: '',
        leaseType: '',
        paId: ''
      }
    }
  }
}
</script>