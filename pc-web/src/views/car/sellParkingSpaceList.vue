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
            <el-form-item :label="$t('sellParkingSpace.owner')" prop="ownerId">
              <div class="flex">
                <el-input :value="ownerName" disabled
                  :placeholder="$t('sellParkingSpace.ownerPlaceholder')"></el-input>
                <el-button type="primary" class="owner-select-btn" @click="openOwnerDialog">{{ $t('sellParkingSpace.selectOwner') }}</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.carNum')" prop="carId">
              <el-select v-model="sellParkingSpaceInfo.carId" filterable clearable
                :loading="carLoading" class="w-full"
                :disabled="!sellParkingSpaceInfo.ownerId"
                :placeholder="$t('sellParkingSpace.carNumSelectPlaceholder')">
                <el-option v-for="car in carList" :key="car.carId"
                  :label="car.carNum" :value="car.carId"></el-option>
              </el-select>
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

    <search-owner ref="searchOwnerRef" @chooseOwner="handleOwnerChange" />
  </div>
</template>

<script>
import SearchOwner from '@/components/owner/SearchOwner'
import { sellParkingSpace, queryCarsWithoutParkingSpace } from '@/api/car/listParkingSpaceApi'
import { getDateStr } from '@/utils/dateUtil'

export default {
  name: 'SellParkingSpaceList',
  components: { SearchOwner },
  data() {
    return {
      saving: false,
      carLoading: false,
      ownerName: '',
      carList: [],
      sellParkingSpaceInfo: {
        psId: '',
        num: '',
        paId: '',
        carId: '',
        ownerId: ''
      },
      rules: {
        ownerId: [
          { required: true, message: this.$t('sellParkingSpace.ownerRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    const query = this.$route.query
    this.sellParkingSpaceInfo.psId = query.psId || ''
    this.sellParkingSpaceInfo.num = query.num || ''
    this.sellParkingSpaceInfo.paId = query.paId || ''
  },
  methods: {
    openOwnerDialog() {
      this.$refs.searchOwnerRef.open()
    },
    handleOwnerChange(owner) {
      this.sellParkingSpaceInfo.ownerId = owner.memberId || ''
      this.ownerName = owner.name || ''
      this.sellParkingSpaceInfo.carId = ''
      this.carList = []
      this.searchCars()
    },
    searchCars() {
      if (!this.sellParkingSpaceInfo.ownerId) return
      this.carLoading = true
      queryCarsWithoutParkingSpace({
        page: 1,
        row: 50,
        ownerId: this.sellParkingSpaceInfo.ownerId
      }).then(res => {
        this.carList = res.data || []
      }).catch(error => {
        console.error('查询车辆列表失败:', error)
        this.$message.error(error.message || this.$t('sellParkingSpace.errorMessage'))
      }).finally(() => {
        this.carLoading = false
      })
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
          ownerId: this.sellParkingSpaceInfo.ownerId,
          sellOrHire: 'S',
          startTime: getDateStr(),
          endTime: '2050-01-01 00:00:00',
          storeId: ''
        }
        if (this.sellParkingSpaceInfo.carId) {
          params.carId = this.sellParkingSpaceInfo.carId
          const selectedCar = this.carList.find(car => car.carId === this.sellParkingSpaceInfo.carId)
          if (selectedCar) {
            params.carNum = selectedCar.carNum
          }
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

.owner-select-btn {
  margin-left: 10px;
  flex-shrink: 0;
}
</style>
