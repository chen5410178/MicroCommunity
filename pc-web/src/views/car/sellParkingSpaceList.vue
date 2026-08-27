<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('sellParkingSpace.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="sellParkingSpaceInfo" :rules="rules" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.parkingSpace')" prop="num">
              <el-input :value="sellParkingSpaceInfo.num" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.parkingArea')">
              <el-input :value="sellParkingSpaceInfo.paId" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.carNum')" prop="carId">
              <el-select v-model="sellParkingSpaceInfo.carId" filterable class="w-full"
                :placeholder="$t('sellParkingSpace.carNumPlaceholder')" @change="handleCarChange">
                <el-option v-for="car in carList" :key="car.carId" :label="car.carNum" :value="car.carId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.owner')" prop="ownerName">
              <el-input :value="sellParkingSpaceInfo.ownerName" disabled
                :placeholder="$t('sellParkingSpace.ownerPlaceholder')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="mt-20 text-right">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="submitSell" :loading="saving">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { sellParkingSpace, queryOwnerCars } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'SellParkingSpaceList',
  data() {
    return {
      saving: false,
      carList: [],
      sellParkingSpaceInfo: {
        psId: '',
        num: '',
        paId: '',
        carId: '',
        ownerId: '',
        ownerName: ''
      },
      rules: {
        carId: [
          { required: true, message: this.$t('sellParkingSpace.carNumRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    const query = this.$route.query
    this.sellParkingSpaceInfo.psId = query.psId || ''
    this.sellParkingSpaceInfo.num = query.num || ''
    this.sellParkingSpaceInfo.paId = query.paId || ''
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
        this.sellParkingSpaceInfo.ownerId = car.ownerId || ''
        this.sellParkingSpaceInfo.ownerName = car.ownerName || ''
      } else {
        this.sellParkingSpaceInfo.ownerId = ''
        this.sellParkingSpaceInfo.ownerName = ''
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    submitSell() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.saving = true
        const params = {
          psId: this.sellParkingSpaceInfo.psId,
          carId: this.sellParkingSpaceInfo.carId,
          ownerId: this.sellParkingSpaceInfo.ownerId,
          sellOrHire: 'S',
          storeId: ''
        }
        sellParkingSpace(params).then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        }).catch(error => {
          console.error('出售车位失败:', error)
          this.$message.error(error.message || this.$t('sellParkingSpace.errorMessage'))
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
