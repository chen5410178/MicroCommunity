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
            <el-form-item :label="$t('updateParkingSpaceCar.owner')" prop="ownerId">
              <div class="flex">
                <el-input :value="ownerName" disabled
                  :placeholder="$t('updateParkingSpaceCar.ownerPlaceholder')"></el-input>
                <el-button type="primary" class="owner-select-btn" @click="openOwnerDialog">{{ $t('updateParkingSpaceCar.selectOwner') }}</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('updateParkingSpaceCar.newCarNum')" prop="carId">
              <el-select v-model="updateParkingSpaceCarInfo.carId" filterable clearable
                :loading="carLoading" class="w-full"
                :disabled="!updateParkingSpaceCarInfo.ownerId"
                :placeholder="$t('updateParkingSpaceCar.carNumSelectPlaceholder')">
                <el-option v-for="car in carList" :key="car.carId"
                  :label="car.carNum" :value="car.carId"></el-option>
              </el-select>
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

    <search-owner ref="searchOwnerRef" @chooseOwner="handleOwnerChange" />
  </div>
</template>

<script>
import SearchOwner from '@/components/owner/SearchOwner'
import { updateParkingSpaceCar, listParkingSpaces, queryCarsWithoutParkingSpace } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'UpdateParkingSpaceCarList',
  components: { SearchOwner },
  data() {
    return {
      saving: false,
      carLoading: false,
      ownerName: '',
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
        startTime: '',
        endTime: ''
      },
      rules: {
        ownerId: [
          { required: true, message: this.$t('updateParkingSpaceCar.ownerRequired'), trigger: 'change' }
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
    this.updateParkingSpaceCarInfo.psId = this.$route.query.psId || ''
    this.loadParkingSpace()
  },
  methods: {
    loadParkingSpace() {
      listParkingSpaces({
        psId: this.updateParkingSpaceCarInfo.psId,
        page: 1,
        row: 10
      }).then(res => {
        const parkingSpaces = res.parkingSpaces || []
        if (parkingSpaces.length < 1) {
          this.$message.error(this.$t('updateParkingSpaceCar.errorMessage'))
          return
        }
        const ps = parkingSpaces[0]
        this.updateParkingSpaceCarInfo.num = ps.num || ''
        this.updateParkingSpaceCarInfo.state = ps.state || ''
        this.updateParkingSpaceCarInfo.currentOwnerName = ps.ownerName || ''
        this.updateParkingSpaceCarInfo.currentCarNum = ps.carNum || ''
        // 默认带出当前业主，并加载其名下无车位车辆
        this.updateParkingSpaceCarInfo.ownerId = ps.ownerId || ''
        this.ownerName = ps.ownerName || ''
        if (ps.startTime && ps.endTime) {
          this.updateParkingSpaceCarInfo.currentRentTime = ps.startTime + ' ~ ' + ps.endTime
          this.updateParkingSpaceCarInfo.startTime = ps.startTime
          this.updateParkingSpaceCarInfo.endTime = ps.endTime
        }
        this.searchCars()
      }).catch(error => {
        console.error('加载车位信息失败:', error)
        this.$message.error(error.message || this.$t('updateParkingSpaceCar.errorMessage'))
      })
    },
    openOwnerDialog() {
      this.$refs.searchOwnerRef.open()
    },
    handleOwnerChange(owner) {
      this.updateParkingSpaceCarInfo.ownerId = owner.memberId || ''
      this.ownerName = owner.name || ''
      this.updateParkingSpaceCarInfo.carId = ''
      this.carList = []
      this.searchCars()
    },
    searchCars() {
      if (!this.updateParkingSpaceCarInfo.ownerId) return
      this.carLoading = true
      queryCarsWithoutParkingSpace({
        page: 1,
        row: 50,
        ownerId: this.updateParkingSpaceCarInfo.ownerId
      }).then(res => {
        this.carList = res.data || []
      }).catch(error => {
        console.error('查询车辆列表失败:', error)
        this.$message.error(error.message || this.$t('updateParkingSpaceCar.errorMessage'))
      }).finally(() => {
        this.carLoading = false
      })
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
          ownerId: this.updateParkingSpaceCarInfo.ownerId
        }
        if (this.updateParkingSpaceCarInfo.carId) {
          params.carId = this.updateParkingSpaceCarInfo.carId
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

.owner-select-btn {
  margin-left: 10px;
  flex-shrink: 0;
}
</style>
