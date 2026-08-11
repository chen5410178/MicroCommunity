<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>{{ $t('carAddParkingSpace.vehicleInfo') }}</span>
          </div>
          <div>
            <el-form label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('carAddParkingSpace.parkingLot')">
                    <el-input v-model="carAddParkingSpaceInfo.areaNum" disabled
                      :placeholder="$t('hireParkingSpace.parkingAreaPlaceholder')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('hireParkingSpace.parkingSpace')" prop="psName" required>
                    <el-input v-model="carAddParkingSpaceInfo.psName" disabled style="width: 80%;"
                      :placeholder="$t('hireParkingSpace.parkingSpacePlaceholder')" />
                    <el-button type="primary" size="small" class="margin-left" @click="openSearchParkingSpaceModel">
                      {{ $t('common.select') }}
                    </el-button>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('carAddParkingSpace.licensePlate')">
                    <el-input v-model.trim="carAddParkingSpaceInfo.carNum"
                      :placeholder="$t('carAddParkingSpace.requiredLicensePlate')" disabled />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('carAddParkingSpace.startTime')">
                    <el-date-picker v-model="carAddParkingSpaceInfo.startTime" style="width: 100%;" type="date"
                      :placeholder="$t('carAddParkingSpace.requiredStartTime')" value-format="yyyy-MM-dd" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('carAddParkingSpace.endTime')">
                    <el-date-picker v-model="carAddParkingSpaceInfo.endTime" style="width: 100%;" type="date"
                      :placeholder="$t('carAddParkingSpace.requiredEndTime')" value-format="yyyy-MM-dd" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="24">
                  <el-form-item :label="$t('carAddParkingSpace.remark')">
                    <el-input v-model.trim="carAddParkingSpaceInfo.remark" type="textarea"
                      :placeholder="$t('carAddParkingSpace.remarkPlaceholder')" rows="3" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24" style="text-align: right; margin-top: 20px;">
        <el-button type="primary" @click="submitForm">
          {{ $t('carAddParkingSpace.submit') }}
        </el-button>
      </el-col>
    </el-row>
    <search-parking-space ref="searchParkingSpace" @choose-parking-space="handleChooseParkingSpace" />
  </div>
</template>

<script>
import { getCarInfo, addCarParkingSpace } from '@/api/car/carAddParkingSpaceApi'
import SearchParkingSpace from '@/components/car/SearchParkingSpace'

export default {
  name: 'CarAddParkingSpaceList',
  components: {
    SearchParkingSpace
  },
  data() {
    return {
      callBackComponent: 'carAddParkingSpace',
      callBackFunction: 'notify',
      carAddParkingSpaceInfo: {
        carNum: '',
        remark: '',
        startTime: '',
        endTime: '',
        carId: '',
        psId: '',
        psName: '',
        memberId: '',
        paId: '',
        areaNum: ''
      }
    }
  },
  created() {
    this.carAddParkingSpaceInfo.carId = this.$route.query.carId
    this.loadCarInfo()
  },
  methods: {
    async loadCarInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          carId: this.carAddParkingSpaceInfo.carId
        }

        const res = await getCarInfo(params)
        if (res.code === 0 && res.data.length > 0) {
          const carData = res.data[0]
          this.carAddParkingSpaceInfo = {
            ...this.carAddParkingSpaceInfo,
            carNum: carData.carNum || '',
            psId: carData.psId || '',
            paId: carData.paId || '',
            areaNum: carData.areaNum || ''
          }
        }
      } catch (error) {
        console.error('Failed to load car info:', error)
      }
    },
    handleParkingSpaceSelected(info) {
      this.carAddParkingSpaceInfo.psId = info.psId
    },
    validateForm() {
      if (!this.carAddParkingSpaceInfo.carNum) {
        this.$message.error(this.$t('carAddParkingSpace.requiredLicensePlate'))
        return false
      }
      if (!this.carAddParkingSpaceInfo.startTime) {
        this.$message.error(this.$t('carAddParkingSpace.requiredStartTime'))
        return false
      }
      if (!this.carAddParkingSpaceInfo.endTime) {
        this.$message.error(this.$t('carAddParkingSpace.requiredEndTime'))
        return false
      }
      if (!this.carAddParkingSpaceInfo.psId) {
        this.$message.error(this.$t('carAddParkingSpace.parkingSpaceRequired'))
        return false
      }

      const start = new Date(this.carAddParkingSpaceInfo.startTime)
      const end = new Date(this.carAddParkingSpaceInfo.endTime)
      if (start >= end) {
        this.$message.error(this.$t('carAddParkingSpace.endTimeAfterStart'))
        return false
      }

      return true
    },
    async submitForm() {
      if (!this.validateForm()) return

      try {
        const data = {
          ...this.carAddParkingSpaceInfo,
          memberId: this.carId
        }

        const res = await addCarParkingSpace(data)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg || this.$t('carAddParkingSpace.submitFailed'))
        }
      } catch (error) {
        console.error('Submit failed:', error)
        this.$message.error(this.$t('carAddParkingSpace.submitFailed'))
      }
    },
    openSearchParkingSpaceModel() {
      this.$refs.searchParkingSpace.open(this.carAddParkingSpaceInfo.paId)
    },
    handleChooseParkingSpace(parkingSpace) {
      this.carAddParkingSpaceInfo.psName = `${parkingSpace.areaNum}-${parkingSpace.num}`
      this.carAddParkingSpaceInfo.psId = parkingSpace.psId
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.el-row {
  margin-bottom: 20px;
}

.margin-left {
  margin-left: 10px;
}
</style>