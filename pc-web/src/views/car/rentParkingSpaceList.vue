<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('rentParkingSpace.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="rentParkingSpaceInfo" :rules="rules" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('rentParkingSpace.parkingSpace')" prop="num">
              <el-input :value="rentParkingSpaceInfo.num" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('rentParkingSpace.parkingArea')">
              <el-input :value="rentParkingSpaceInfo.paId" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('rentParkingSpace.carNum')" prop="carId">
              <div class="flex">
                <el-input :value="selectedCarNum" disabled
                  :placeholder="$t('rentParkingSpace.carNumPlaceholder')"></el-input>
                <el-button type="primary" class="car-select-btn" @click="openCarDialog">{{ $t('rentParkingSpace.selectCar') }}</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('rentParkingSpace.owner')" prop="ownerName">
              <el-input :value="rentParkingSpaceInfo.ownerName" disabled
                :placeholder="$t('rentParkingSpace.ownerPlaceholder')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('rentParkingSpace.startTime')" prop="startTime">
              <el-date-picker v-model="rentParkingSpaceInfo.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss" class="w-full"
                :placeholder="$t('rentParkingSpace.startTimePlaceholder')"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('rentParkingSpace.endTime')" prop="endTime">
              <el-date-picker v-model="rentParkingSpaceInfo.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss" class="w-full"
                :placeholder="$t('rentParkingSpace.endTimePlaceholder')"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="mt-20 text-right">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="submitRent" :loading="saving">
        {{ $t('common.submit') }}
      </el-button>
    </div>

    <select-no-ps-car ref="selectNoPsCarRef" @select="onSelectCar" />
  </div>
</template>

<script>
import SelectNoPsCar from '@/components/car/selectNoPsCar'
import { sellParkingSpace } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'RentParkingSpaceList',
  components: { SelectNoPsCar },
  data() {
    return {
      saving: false,
      selectedCarNum: '',
      rentParkingSpaceInfo: {
        psId: '',
        num: '',
        paId: '',
        carId: '',
        ownerId: '',
        ownerName: '',
        startTime: '',
        endTime: ''
      },
      rules: {
        carId: [
          { required: true, message: this.$t('rentParkingSpace.carNumRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('rentParkingSpace.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('rentParkingSpace.endTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    const query = this.$route.query
    this.rentParkingSpaceInfo.psId = query.psId || ''
    this.rentParkingSpaceInfo.num = query.num || ''
    this.rentParkingSpaceInfo.paId = query.paId || ''
  },
  methods: {
    openCarDialog() {
      this.$refs.selectNoPsCarRef.open()
    },
    onSelectCar(row) {
      this.rentParkingSpaceInfo.carId = row.carId || ''
      this.rentParkingSpaceInfo.ownerId = row.ownerId || ''
      this.rentParkingSpaceInfo.ownerName = row.ownerName || ''
      this.selectedCarNum = row.carNum || ''
    },
    goBack() {
      this.$router.go(-1)
    },
    submitRent() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.rentParkingSpaceInfo.endTime <= this.rentParkingSpaceInfo.startTime) {
          this.$message.error(this.$t('rentParkingSpace.timeError'))
          return
        }
        this.saving = true
        const params = {
          psId: this.rentParkingSpaceInfo.psId,
          carId: this.rentParkingSpaceInfo.carId,
          ownerId: this.rentParkingSpaceInfo.ownerId,
          sellOrHire: 'H',
          startTime: this.rentParkingSpaceInfo.startTime,
          endTime: this.rentParkingSpaceInfo.endTime,
          storeId: ''
        }
        sellParkingSpace(params).then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        }).catch(error => {
          console.error('出租车位失败:', error)
          this.$message.error(error.message || this.$t('rentParkingSpace.errorMessage'))
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

.car-select-btn {
  margin-left: 10px;
  flex-shrink: 0;
}
</style>
