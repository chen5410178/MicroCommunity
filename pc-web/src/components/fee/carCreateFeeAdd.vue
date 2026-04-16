<template>
  <div class="car-create-fee-add">
    <el-dialog :title="$t('carCreateFeeAdd.createFee')" :visible.sync="dialogVisible" width="800px"
      :before-close="handleClose">
      <el-form :model="carCreateFeeAddInfo" :rules="rules" ref="createForm" label-width="120px">
        <el-form-item :label="$t('carCreateFeeAdd.car')">
          <el-input v-model="carCreateFeeAddInfo.locationTypeCdName" disabled
            :placeholder="$t('carCreateFeeAdd.enterCar')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('carCreateFeeAdd.feeType')" prop="feeTypeCd">
          <el-select v-model="carCreateFeeAddInfo.feeTypeCd" @change="_changeCarFeeTypeCd" style="width:100%">
            <el-option :label="$t('carCreateFeeAdd.selectFeeType')" value="" disabled></el-option>
            <el-option v-for="(item, index) in filteredFeeTypeCds" :key="index" :label="item.name"
              :value="item.statusCd"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('carCreateFeeAdd.feeItem')" prop="configId">
          <el-select v-model="carCreateFeeAddInfo.configId" @change="_carCreateFeeAddIfOnceFee" style="width:100%">
            <el-option :label="$t('carCreateFeeAdd.selectFeeItem')" value="" disabled></el-option>
            <el-option v-for="(item, index) in carCreateFeeAddInfo.feeConfigs" :key="index" :label="item.feeName"
              :value="item.configId"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="carCreateFeeAddInfo.computingFormula === '4004'" :label="$t('carCreateFeeAdd.feeAmount')"
          prop="amount">
          <el-input v-model="carCreateFeeAddInfo.amount" :placeholder="$t('carCreateFeeAdd.enterFeeAmount')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('carCreateFeeAdd.billingStartTime')" prop="startTime">
          <el-date-picker v-model="carCreateFeeAddInfo.startTime" type="datetime"
            :placeholder="$t('carCreateFeeAdd.enterBillingStartTime')" value-format="yyyy-MM-dd HH:mm:ss"
            style="width:100%" @change="validateStartTime"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('carCreateFeeAdd.billingEndTime')" prop="endTime">
          <el-date-picker v-model="carCreateFeeAddInfo.endTime" type="datetime"
            :placeholder="$t('carCreateFeeAdd.enterBillingEndTime')" value-format="yyyy-MM-dd HH:mm:ss"
            style="width:100%" @change="validateEndTime"></el-date-picker>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="submitForm" :loading="loading">
          {{ $t('common.submit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { saveParkingSpaceCreateFee, listFeeConfigs, listParkingAreas } from '@/api/fee/carCreateFeeApi'

export default {
  name: 'CarCreateFeeAdd',
  data() {
    return {
      communityId: '',
      dialogVisible: false,
      loading: false,
      carCreateFeeAddInfo: {
        feeTypeCds: [],
        feeConfigs: [],
        parkingAreas: [],
        locationObjId: '',
        locationTypeCd: '2000',
        carId: '',
        feeTypeCd: '',
        configId: '',
        carState: ['H', 'S'],
        isMore: false,
        locationTypeCdName: '',
        startTime: '',
        paId: '',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        endTime: ''
      },
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('carCreateFeeAdd.feeTypeRequired'), trigger: 'change' }
        ],
        configId: [
          { required: true, message: this.$t('carCreateFeeAdd.feeItemRequired'), trigger: 'change' }
        ],
        carState: [
          { required: true, message: this.$t('carCreateFeeAdd.billingTypeRequired') }
        ],
        startTime: [
          { required: true, message: this.$t('carCreateFeeAdd.billingStartTimeRequired'), trigger: 'change' },
          { validator: this.validateDate, trigger: 'change' }
        ],
        amount: [
          { required: true, message: this.$t('carCreateFeeAdd.amountRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('carCreateFeeAdd.amountFormatError'), trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    filteredFeeTypeCds() {
      return (this.carCreateFeeAddInfo.feeTypeCds || []).filter(item =>
        item.statusCd !== '888800010009' && item.statusCd !== '888800010011'
      )
    }
  },
  async created() {
    this.communityId = await getCommunityId()
    await this.loadDictData()
    await this.loadParkingAreas()
    this.carCreateFeeAddInfo.startTime = this.formatDateTime(new Date())
  },
  methods: {
    async loadDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.carCreateFeeAddInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async loadParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 20,
          communityId: this.communityId
        }

        const response = await listParkingAreas(params)
        this.carCreateFeeAddInfo.parkingAreas = response.parkingAreas || []
      } catch (error) {
        console.error('获取停车场数据失败:', error)
      }
    },

    open(createData) {
      this.clearAddFeeConfigInfo()
      this.carCreateFeeAddInfo.locationTypeCd = '2000'
      this.carCreateFeeAddInfo.locationObjId = createData.carId || createData.car.carId
      this.carCreateFeeAddInfo.carId = createData.carId || createData.car.carId
      this.carCreateFeeAddInfo.locationTypeCdName = createData.carNum || createData.car.carNum
      this.carCreateFeeAddInfo.isMore = createData.isMore || false

      this.dialogVisible = true

      this.$nextTick(() => {
        if (this.$refs.createForm) {
          this.$refs.createForm.clearValidate()
        }
      })
    },

    async _changeCarFeeTypeCd(feeTypeCd) {
      if (!feeTypeCd) return

      try {
        const params = {
          page: 1,
          row: 20,
          communityId: this.communityId,
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          valid: '1'
        }

        const response = await listFeeConfigs(params)
        this.carCreateFeeAddInfo.feeConfigs = response.feeConfigs || []
      } catch (error) {
        console.error('获取费用配置失败:', error)
      }
    },

    _carCreateFeeAddIfOnceFee(configId) {
      this.carCreateFeeAddInfo.endTime = ''
      const config = this.carCreateFeeAddInfo.feeConfigs.find(item => item.configId === configId)
      if (config) {
        this.carCreateFeeAddInfo.feeFlag = config.feeFlag
        this.carCreateFeeAddInfo.computingFormula = config.computingFormula
      }
    },

    validateStartTime() {
      if (this.carCreateFeeAddInfo.startTime && this.carCreateFeeAddInfo.endTime) {
        const start = new Date(this.carCreateFeeAddInfo.startTime)
        const end = new Date(this.carCreateFeeAddInfo.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('carCreateFeeAdd.endTimeAfterStart'))
          this.carCreateFeeAddInfo.endTime = ''
        }
      }
    },

    validateEndTime() {
      if (this.carCreateFeeAddInfo.startTime && this.carCreateFeeAddInfo.endTime) {
        const start = new Date(this.carCreateFeeAddInfo.startTime)
        const end = new Date(this.carCreateFeeAddInfo.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('carCreateFeeAdd.endTimeAfterStart'))
          this.carCreateFeeAddInfo.endTime = ''
        }
      }
    },

    validateDate(rule, value, callback) {
      if (!value) {
        callback(new Error(rule.message))
        return
      }

      const date = new Date(value)
      if (isNaN(date.getTime())) {
        callback(new Error(this.$t('carCreateFeeAdd.dateFormatError')))
        return
      }

      callback()
    },

    submitForm() {
      this.$refs.createForm.validate(async (valid) => {
        if (!valid) {
          return
        }

        try {
          this.loading = true
          const data = {
            ...this.carCreateFeeAddInfo,
            communityId: this.communityId
          }

          await saveParkingSpaceCreateFee(data)

          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()

        } catch (error) {
          console.error('创建车辆费用失败:', error)
          this.$message.error(this.$t('carCreateFeeAdd.createFailed'))
        } finally {
          this.loading = false
        }
      })
    },

    formatDateTime(date) {
      if (!date) return ''
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    clearAddFeeConfigInfo() {
      const feeTypeCds = this.carCreateFeeAddInfo.feeTypeCds
      const parkingAreas = this.carCreateFeeAddInfo.parkingAreas
      this.carCreateFeeAddInfo = {
        feeTypeCds: feeTypeCds,
        feeConfigs: [],
        parkingAreas: parkingAreas,
        locationTypeCd: '2000',
        locationObjId: '',
        carId: '',
        feeTypeCd: '',
        configId: '',
        carState: ['H', 'S'],
        isMore: false,
        locationTypeCdName: '',
        startTime: this.formatDateTime(new Date()),
        paId: '',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        endTime: ''
      }
    },

    handleClose() {
      this.dialogVisible = false
      this.clearAddFeeConfigInfo()
      this.loading = false

      if (this.$refs.createForm) {
        this.$refs.createForm.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.car-create-fee-add {
  .dialog-footer {
    text-align: right;
  }
}

::v-deep .el-dialog__body {
  padding: 20px;
}
</style>