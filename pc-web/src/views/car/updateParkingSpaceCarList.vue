<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('updateParkingSpaceCar.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="updateParkingSpaceCarInfo" :rules="rules" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.parkingSpace')" prop="num">
              <el-input :value="updateParkingSpaceCarInfo.num" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.currentOwner')">
              <el-input :value="updateParkingSpaceCarInfo.currentOwnerName" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.currentCarNum')">
              <el-input :value="updateParkingSpaceCarInfo.currentCarNum" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="updateParkingSpaceCarInfo.state === 'H'">
            <el-form-item :label="$t('updateParkingSpaceCar.currentRentTime')">
              <el-input :value="updateParkingSpaceCarInfo.currentRentTime" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider></el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.newCarNum')" prop="carId">
              <el-select v-model="updateParkingSpaceCarInfo.carId" filterable class="w-full"
                :placeholder="$t('updateParkingSpaceCar.carNumPlaceholder')" @change="handleCarChange">
                <el-option v-for="car in carList" :key="car.carId" :label="car.carNum" :value="car.carId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.owner')" prop="ownerName">
              <el-input :value="updateParkingSpaceCarInfo.ownerName" disabled
                :placeholder="$t('updateParkingSpaceCar.ownerPlaceholder')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="updateParkingSpaceCarInfo.state === 'H'" :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.startTime')" prop="startTime">
              <el-date-picker v-model="updateParkingSpaceCarInfo.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss" class="w-full"
                :placeholder="$t('updateParkingSpaceCar.startTimePlaceholder')"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.endTime')" prop="endTime">
              <el-date-picker v-model="updateParkingSpaceCarInfo.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss" class="w-full"
                :placeholder="$t('updateParkingSpaceCar.endTimePlaceholder')"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="mt-20 text-right">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="submitUpdate" :loading="saving">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { updateParkingSpaceCar, queryOwnerCars } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'UpdateParkingSpaceCarList',
  data() {
    return {
      saving: false,
      carList: [],
      updateParkingSpaceCarInfo: {
        psId: '',
        num: '',
        state: '',
        currentOwnerName: '',
        currentCarNum: '',
        currentRentTime: '',
        carId: '',
        ownerId: '',
        ownerName: '',
        startTime: '',
        endTime: ''
      },
      rules: {
        carId: [
          { required: true, message: this.$t('updateParkingSpaceCar.carNumRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('updateParkingSpaceCar.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('updateParkingSpaceCar.endTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    const query = this.$route.query
    this.updateParkingSpaceCarInfo.psId = query.psId || ''
    this.updateParkingSpaceCarInfo.num = query.num || ''
    this.updateParkingSpaceCarInfo.state = query.state || ''
    this.updateParkingSpaceCarInfo.currentOwnerName = query.ownerName || ''
    this.updateParkingSpaceCarInfo.currentCarNum = query.carNum || ''
    if (query.startTime && query.endTime) {
      this.updateParkingSpaceCarInfo.currentRentTime = query.startTime + ' ~ ' + query.endTime
      this.updateParkingSpaceCarInfo.startTime = query.startTime
      this.updateParkingSpaceCarInfo.endTime = query.endTime
    }
    this.loadCars()
  },
  methods: {
    async loadCars() {
      try {
        const params = {
          page: 1,
          row: 100
        }
        const res = await queryOwnerCars(params)
        this.carList = res.data || []
      } catch (error) {
        console.error('加载车辆列表失败:', error)
      }
    },
    handleCarChange(carId) {
      const car = this.carList.find(item => item.carId === carId)
      if (car) {
        this.updateParkingSpaceCarInfo.ownerId = car.ownerId || ''
        this.updateParkingSpaceCarInfo.ownerName = car.ownerName || ''
      } else {
        this.updateParkingSpaceCarInfo.ownerId = ''
        this.updateParkingSpaceCarInfo.ownerName = ''
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    submitUpdate() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.updateParkingSpaceCarInfo.state === 'H') {
          if (this.updateParkingSpaceCarInfo.endTime <= this.updateParkingSpaceCarInfo.startTime) {
            this.$message.error(this.$t('updateParkingSpaceCar.timeError'))
            return
          }
        }
        this.saving = true
        const params = {
          psId: this.updateParkingSpaceCarInfo.psId,
          carId: this.updateParkingSpaceCarInfo.carId,
          ownerId: this.updateParkingSpaceCarInfo.ownerId
        }
        if (this.updateParkingSpaceCarInfo.state === 'H') {
          params.startTime = this.updateParkingSpaceCarInfo.startTime
          params.endTime = this.updateParkingSpaceCarInfo.endTime
        }
        updateParkingSpaceCar(params).then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        }).catch(error => {
          console.error('修改车位车辆失败:', error)
          this.$message.error(error.message || this.$t('updateParkingSpaceCar.errorMessage'))
        }).finally(() => {
          this.saving = false
        })
      })
    }
  }
}
</script>

<style scoped>
.w-full {
  width: 100%;
}

.mt-20 {
  margin-top: 20px;
}
</style>
