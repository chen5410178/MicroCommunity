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
              <div class="flex">
                <el-input :value="selectedCarNum" disabled
                  :placeholder="$t('sellParkingSpace.carNumPlaceholder')"></el-input>
                <el-button type="primary" class="car-select-btn" @click="openCarDialog">{{ $t('sellParkingSpace.selectCar') }}</el-button>
              </div>
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

    <select-no-ps-car ref="selectNoPsCarRef" @select="onSelectCar" />
  </div>
</template>

<script>
import SelectNoPsCar from '@/components/car/selectNoPsCar'
import { sellParkingSpace } from '@/api/car/listParkingSpaceApi'
import { getDateStr } from '@/utils/dateUtil'

export default {
  name: 'SellParkingSpaceList',
  components: { SelectNoPsCar },
  data() {
    return {
      saving: false,
      selectedCarNum: '',
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
  },
  methods: {
    openCarDialog() {
      this.$refs.selectNoPsCarRef.open()
    },
    onSelectCar(row) {
      this.sellParkingSpaceInfo.carId = row.carId || ''
      this.sellParkingSpaceInfo.ownerId = row.ownerId || ''
      this.sellParkingSpaceInfo.ownerName = row.ownerName || ''
      this.selectedCarNum = row.carNum || ''
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
          startTime: getDateStr(),
          endTime: '2050-01-01 00:00:00',
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

.car-select-btn {
  margin-left: 10px;
  flex-shrink: 0;
}
</style>
