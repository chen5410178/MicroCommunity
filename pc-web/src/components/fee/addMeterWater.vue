<template>
  <el-dialog :title="$t('meterWater.addMeterReading')" :visible.sync="dialogVisible" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('meterWater.feeType')" prop="feeTypeCd">
        <el-select v-model="form.feeTypeCd" :placeholder="$t('meterWater.selectFeeType')" style="width: 100%"
          @change="handleFeeTypeChange">
          <el-option v-for="item in feeTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('meterWater.feeItem')" prop="configId">
        <el-select v-model="form.configId" :placeholder="$t('meterWater.selectFeeItem')" style="width: 100%"
          @change="handleConfigChange">
          <el-option v-for="item in feeConfigs" :key="item.configId" :label="item.feeName" :value="item.configId" />
        </el-select>
        <div class="form-tip">
          {{ $t('meterWater.feeItemTip') }}
        </div>
      </el-form-item>

      <el-form-item :label="$t('meterWater.meterType')" prop="meterType">
        <el-select v-model="form.meterType" :placeholder="$t('meterWater.selectMeterType')" style="width: 100%"
          @change="handleMeterTypeChange">
          <el-option v-for="item in meterTypes" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>

      <template v-if="!form.hasRoom">
        <el-form-item :label="$t('meterWater.building')">
          <floor-select2 ref="floorSelect" @change="handleFloorChange" />
        </el-form-item>

        <el-form-item :label="$t('meterWater.unit')">
          <unit-select2 ref="unitSelect" @change="handleUnitChange" />
        </el-form-item>

        <el-form-item :label="$t('meterWater.room')">
          <room-select2 ref="roomSelect" @change="handleRoomChange" />
        </el-form-item>
      </template>

      <el-form-item v-else :label="$t('meterWater.feeObject')">
        <el-input v-model="form.ownerName" :placeholder="$t('meterWater.inputRoom')" disabled />
      </el-form-item>

      <el-form-item :label="$t('meterWater.preDegrees')" prop="preDegrees">
        <el-input v-model="form.preDegrees" :placeholder="$t('meterWater.inputPreDegrees')" />
      </el-form-item>

      <el-form-item :label="$t('meterWater.curDegrees')" prop="curDegrees">
        <el-input v-model="form.curDegrees" :placeholder="$t('meterWater.inputCurDegrees')"
          @change="handleDegreesChange" />
      </el-form-item>

      <el-form-item :label="$t('meterWater.preReadingTime')" prop="preReadingTime">
        <el-date-picker v-model="form.preReadingTime" type="datetime"
          :placeholder="$t('meterWater.selectPreReadingTime')" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
      </el-form-item>

      <el-form-item :label="$t('meterWater.curReadingTime')" prop="curReadingTime">
        <el-date-picker v-model="form.curReadingTime" type="datetime"
          :placeholder="$t('meterWater.selectCurReadingTime')" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
      </el-form-item>

      <el-form-item v-if="form.computingFormula === '9009'" :label="$t('meterWater.price')" prop="price">
        <el-input v-model="form.price" :placeholder="$t('meterWater.inputPrice')" />
      </el-form-item>

      <el-form-item :label="$t('meterWater.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('meterWater.inputRemark')" :rows="2" />
      </el-form-item>

      <el-form-item>
        <div class="form-tip">
          {{ $t('meterWater.unitTip') }}
        </div>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveMeterWater, queryPreMeterWater } from '@/api/fee/meterWaterManageApi'
import { listFeeConfigs, listMeterTypes } from '@/api/fee/meterWaterManageApi'
//import { getDict } from '@/api/community/communityApi'
import FloorSelect2 from '@/components/fee/floorSelect2'
import UnitSelect2 from '@/components/fee/unitSelect2'
import RoomSelect2 from '@/components/fee/roomSelect2'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddMeterWater',
  components: {
    FloorSelect2,
    UnitSelect2,
    RoomSelect2
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        waterId: '',
        meterType: '',
        preDegrees: '',
        curDegrees: '',
        preReadingTime: '',
        curReadingTime: '',
        remark: '',
        roomId: '',
        objId: '',
        objName: '',
        feeTypeCd: '',
        feeConfigs: [],
        configId: '',
        objType: '3333',
        hasRoom: false,
        ownerName: '',
        meterTypes: [],
        computingFormula: '',
        price: 0,
        communityId: ''
      },
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('meterWater.feeTypeRequired'), trigger: 'blur' }
        ],
        configId: [
          { required: true, message: this.$t('meterWater.feeItemRequired'), trigger: 'blur' }
        ],
        meterType: [
          { required: true, message: this.$t('meterWater.meterTypeRequired'), trigger: 'blur' }
        ],
        preDegrees: [
          { required: true, message: this.$t('meterWater.preDegreesRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('meterWater.degreesFormatError') }
        ],
        curDegrees: [
          { required: true, message: this.$t('meterWater.curDegreesRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('meterWater.degreesFormatError') }
        ],
        preReadingTime: [
          { required: true, message: this.$t('meterWater.preReadingTimeRequired'), trigger: 'blur' }
        ],
        curReadingTime: [
          { required: true, message: this.$t('meterWater.curReadingTimeRequired'), trigger: 'blur' }
        ],
        remark: [
          { max: 500, message: this.$t('meterWater.remarkMaxLength'), trigger: 'blur' }
        ]
      },
      feeTypeOptions: [],
      feeConfigs: [],
      meterTypes: []
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.loadFeeTypes()
    this.loadMeterTypes()
  },
  methods: {
    open(params) {
      this.resetForm()
      if (params) {
        this.form.hasRoom = Object.prototype.hasOwnProperty.call(params, 'roomId')
        if (params.roomId) {
          this.form.roomId = params.roomId
          this.form.objId = params.roomId
          this.form.objName = params.roomName
          this.form.ownerName = params.ownerName
            ? `${params.roomName}(${params.ownerName})`
            : params.roomName
        }
      }
      this.dialogVisible = true
    },
    resetForm() {
      this.form = {
        waterId: '',
        meterType: '',
        preDegrees: '',
        curDegrees: '',
        preReadingTime: '',
        curReadingTime: '',
        remark: '',
        roomId: '',
        objId: '',
        objName: '',
        feeTypeCd: '',
        feeConfigs: [],
        configId: '',
        objType: '3333',
        hasRoom: false,
        ownerName: '',
        meterTypes: [],
        computingFormula: '',
        price: 0,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.resetForm()
    },
    async loadFeeTypes() {
      // try {
      //   const data = await getDict('pay_fee_config', 'fee_type_cd')
      //   this.feeTypeOptions = data.map(item => ({
      //     value: item.statusCd,
      //     label: item.name
      //   }))
      // } catch (error) {
      //   console.error('Failed to load fee types:', error)
      // }
      this.feeTypeOptions = [
        { value: '888800010015', label: '水费' },
        { value: '888800010016', label: '电费' },
        { value: '888800010009', label: '煤气费' }
      ]
    },
    async loadMeterTypes() {
      try {
        const { data } = await listMeterTypes({
          communityId: this.form.communityId,
          page: 1,
          row: 50
        })
        this.meterTypes = data
      } catch (error) {
        console.error('Failed to load meter types:', error)
      }
    },
    async handleFeeTypeChange() {
      try {
        const { data } = await listFeeConfigs({
          communityId: this.form.communityId,
          feeTypeCd: this.form.feeTypeCd,
          isDefault: 'F',
          page: 1,
          row: 20
        })
        this.feeConfigs = data
        this.form.configId = ''
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    handleConfigChange(configId) {
      const config = this.feeConfigs.find(item => item.configId === configId)
      if (config) {
        this.form.computingFormula = config.computingFormula
      }
      if (this.form.roomId) {
        this.queryPreMeterWater()
      }
    },
    handleMeterTypeChange() {
      if (this.form.roomId) {
        this.queryPreMeterWater()
      }
    },
    async queryPreMeterWater() {
      try {
        const { data } = await queryPreMeterWater({
          communityId: this.form.communityId,
          objId: this.form.roomId,
          objType: this.form.objType,
          meterType: this.form.meterType
        })

        if (data && data.length > 0) {
          this.form.preDegrees = data[0].curDegrees
          this.form.preReadingTime = data[0].curReadingTime
          if (this.form.computingFormula === '9009') {
            this.form.price = data[0].price
          }
        } else {
          this.form.preDegrees = '0'
          this.form.preReadingTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
        }
      } catch (error) {
        console.error('Failed to query pre meter water:', error)
      }
    },
    handleDegreesChange() {
      const pre = parseFloat(this.form.preDegrees) || 0
      const cur = parseFloat(this.form.curDegrees) || 0
      if (pre > cur) {
        this.$message.warning(this.$t('meterWater.degreesCompareError'))
        this.form.curDegrees = ''
      }
    },
    handleFloorChange(floor) {
      this.form.floorId = floor.floorId
      this.form.floorNum = floor.floorNum
    },
    handleUnitChange(unit) {
      this.form.unitId = unit.unitId
      this.form.unitNum = unit.unitNum
    },
    handleRoomChange(room) {
      this.form.roomId = room.roomId
      this.form.objId = room.roomId
      this.form.objName = room.name
      this.form.ownerName = room.link
      this.queryPreMeterWater()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          await saveMeterWater(this.form)
          this.$message.success(this.$t('common.operationSuccess'))
          this.dialogVisible = false
          this.$emit('success')
        } catch (error) {
          console.error('Failed to save meter water:', error)
          this.$message.error(error.message || this.$t('common.saveFailed'))
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

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