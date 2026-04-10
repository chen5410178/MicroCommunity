<template>
  <el-dialog :title="$t('roomCreateFeeAdd.title')" :visible.sync="visible" width="40%" :before-close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('roomCreateFeeAdd.location')">
        <el-input v-model="formData.locationTypeCdName" disabled
          :placeholder="$t('roomCreateFeeAdd.locationPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('roomCreateFeeAdd.feeType')" prop="feeTypeCd">
        <el-select v-model="formData.feeTypeCd" @change="handleFeeTypeChange"
          :placeholder="$t('roomCreateFeeAdd.feeTypePlaceholder')" style="width: 100%">
          <template v-for="item in feeTypeOptions">
            <el-option v-if="item.statusCd != '888800010017'" :key="item.statusCd" :label="item.name"
              :value="item.statusCd"></el-option>
          </template>
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.feeTypeCd" :label="$t('roomCreateFeeAdd.feeItem')" prop="configId">
        <el-select v-model="formData.configId" @change="handleConfigChange"
          :placeholder="$t('roomCreateFeeAdd.feeItemPlaceholder')" style="width: 100%">
          <el-option v-for="item in feeConfigOptions" :key="item.configId" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item v-if="formData.computingFormula == '4004'" :label="$t('roomCreateFeeAdd.amount')" prop="amount">
        <el-input v-model="formData.amount" :placeholder="$t('roomCreateFeeAdd.amountPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('roomCreateFeeAdd.startTime')" prop="startTime">
        <el-date-picker v-model="formData.startTime" type="date"
          :placeholder="$t('roomCreateFeeAdd.startTimePlaceholder')" value-format="yyyy-MM-dd"
          @change="validateStartTime"></el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('roomCreateFeeAdd.endTime')" prop="endTime">
        <el-date-picker v-model="formData.endTime" type="date" :placeholder="$t('roomCreateFeeAdd.endTimePlaceholder')"
          value-format="yyyy-MM-dd" @change="validateEndTime"></el-date-picker>
      </el-form-item>

      <el-form-item v-show="formData.computingFormula == '1102'" :label="$t('roomCreateFeeAdd.rateCycle')"
        prop="rateCycle">
        <el-input v-model="formData.rateCycle" :placeholder="$t('roomCreateFeeAdd.rateCyclePlaceholder')"></el-input>
      </el-form-item>

      <el-form-item v-show="formData.computingFormula == '1102'" :label="$t('roomCreateFeeAdd.rate')" prop="rate">
        <el-input v-model="formData.rate" :placeholder="$t('roomCreateFeeAdd.ratePlaceholder')"></el-input>
      </el-form-item>

      <el-form-item v-show="formData.computingFormula == '1102'" :label="$t('roomCreateFeeAdd.rateStartTime')"
        prop="rateStartTime">
        <el-date-picker v-model="formData.rateStartTime" type="date"
          :placeholder="$t('roomCreateFeeAdd.rateStartTimePlaceholder')" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveRoomCreateFee, listFeeConfigs } from '@/api/fee/roomCreateFeeAddApi'
import { getDict, getCommunityId } from '@/api/community/communityApi'
import { dateFormat, addMonth } from '@/utils/dateUtil'

export default {
  name: 'RoomCreateFeeAdd',
  data() {
    return {
      visible: false,
      formData: {
        feeTypeCds: [],
        feeConfigs: [],
        locationTypeCd: '',
        locationObjId: '',
        feeTypeCd: '',
        configId: '',
        locationTypeCdName: '',
        startTime: '',
        endTime: '2050-01-01',
        computingFormula: '',
        amount: '',
        rateCycle: '',
        rate: '',
        rateStartTime: '',
        feeFlag: ''
      },
      feeTypeOptions: [],
      feeConfigOptions: [],
      rules: {
        locationObjId: [
          { required: true, message: this.$t('roomCreateFeeAdd.validate.locationRequired'), trigger: 'blur' }
        ],
        feeTypeCd: [
          { required: true, message: this.$t('roomCreateFeeAdd.validate.feeTypeRequired'), trigger: 'change' }
        ],
        configId: [
          { required: true, message: this.$t('roomCreateFeeAdd.validate.feeItemRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('roomCreateFeeAdd.validate.startTimeRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(room) {
      this.resetForm()
      this.formData.locationObjId = room.roomId
      this.formData.locationTypeCdName = `${room.floorNum}-${room.unitNum}-${room.roomNum}(${room.ownerName})`

      if (room.roomName) {
        this.formData.locationTypeCdName = room.roomName
      }

      this.formData.roomType = room.roomType
      if (room.roomType === '2020602') {
        this.formData.locationTypeCdName = `${room.floorNum}-${room.roomNum}(${room.ownerName})`
      }

      this.loadFeeTypes()
      this.visible = true
    },

    loadFeeTypes() {
      getDict('pay_fee_config', 'fee_type_cd').then(data => {
        this.feeTypeOptions = data.filter(item =>
          item.statusCd !== '888800010015' && item.statusCd !== '888800010016'
        )
      })
    },

    handleFeeTypeChange(value) {
      if (value === '888800010006' || value === '888800010014') {
        this.formData.startTime = dateFormat(new Date())
        this.formData.endTime = dateFormat(addMonth(new Date(), 1))
      }

      this.formData.configId = ''
      this.loadFeeConfigs(value)
    },

    loadFeeConfigs(feeTypeCd) {
      listFeeConfigs({
        page: 1,
        row: 500,
        communityId: getCommunityId(),
        feeTypeCd,
        isDefault: 'F',
        state: 'Y'
      }).then(response => {
        this.feeConfigOptions = response.feeConfigs
      })
    },

    handleConfigChange(configId) {
      if (this.formData.feeTypeCd !== '888800010006' &&
        this.formData.feeTypeCd !== '888800010014' &&
        this.formData.feeFlag === '1003006') {
        this.formData.endTime = ''
      }

      const config = this.feeConfigOptions.find(item => item.configId === configId)
      if (config) {
        this.formData.feeFlag = config.feeFlag
        this.formData.computingFormula = config.computingFormula
      }
    },

    validateStartTime(value) {
      if (!this.formData.feeFlag && !this.formData.feeTypeCd) {
        const start = new Date(value)
        const end = new Date(this.formData.endTime)
        if (start >= end) {
          this.$message.error(this.$t('roomCreateFeeAdd.validate.startBeforeEnd'))
          this.formData.startTime = ''
        }
      }
    },

    validateEndTime(value) {
      const start = new Date(this.formData.startTime)
      const end = new Date(value)
      if (start >= end) {
        this.$message.error(this.$t('roomCreateFeeAdd.validate.endAfterStart'))
        this.formData.endTime = ''
      }
    },

    async handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return

        if (this.formData.feeTypeCd !== '888800010006' && this.formData.feeFlag !== '1003006') {
          if (!this.formData.endTime) {
            this.$message.error(this.$t('roomCreateFeeAdd.validate.endTimeRequired'))
            return
          }
        }
      })
      const params = {
        ...this.formData,
        communityId: getCommunityId()
      }

      const { code, msg } = await saveRoomCreateFee(params)
      if (!code ||code == 0) {
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } else {
        this.$message.error(msg)
      }

    },

    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.formData = {
        feeTypeCds: [],
        feeConfigs: [],
        locationTypeCd: '',
        locationObjId: '',
        feeTypeCd: '',
        configId: '',
        locationTypeCdName: '',
        startTime: '',
        endTime: '2050-01-01',
        computingFormula: '',
        amount: '',
        rateCycle: '',
        rate: '',
        rateStartTime: '',
        feeFlag: ''
      }
    },

    handleClose() {
      this.visible = false
      this.resetForm()
    }
  }
}
</script>
<style scoped>
.el-form-item {
  width: 90%;
  text-align: left;
}

.el-select {
  width: 100%;
}

.el-date-editor {
  width: 100%;
}
</style>