<template>
  <el-dialog :title="batchFeeCycleInfo.feeName" :visible.sync="visible" width="500px" :before-close="handleClose">
    <el-form label-width="120px">
      <el-form-item :label="$t('batchFeeCycle.paymentCycle')">
        <el-select v-model="batchFeeCycleInfo.tempCycle" @change="changeTempCycle" style="width:100%"
          :placeholder="$t('batchFeeCycle.selectCycleTip')">
          <el-option value="-100" :label="$t('batchFeeCycle.default')"></el-option>
          <el-option value="-102" :label="$t('batchFeeCycle.customCycle')"></el-option>
          <el-option value="-101" :label="$t('batchFeeCycle.customAmount')"></el-option>
          <el-option value="-103" :label="$t('batchFeeCycle.customEndTime')"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item v-if="batchFeeCycleInfo.tempCycle === '-101'" :label="$t('batchFeeCycle.customAmount')">
        <el-input type="number" v-model="batchFeeCycleInfo.receivedAmount"
          :placeholder="$t('batchFeeCycle.enterCustomAmount')"></el-input>
      </el-form-item>

      <el-form-item v-if="batchFeeCycleInfo.tempCycle === '-102'" :label="$t('batchFeeCycle.actualCycle')">
        <el-input type="number" v-model="batchFeeCycleInfo.cycles"
          :placeholder="$t('batchFeeCycle.enterActualCycle')"></el-input>
      </el-form-item>

      <el-form-item v-if="batchFeeCycleInfo.tempCycle === '-103'" :label="$t('batchFeeCycle.endTime')">
        <el-date-picker v-model="batchFeeCycleInfo.custEndTime" type="date" style="width:100%"
          :placeholder="$t('batchFeeCycle.selectEndTime')" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="_doSubmitFeeCycle">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'BatchFeeCycle',
  data() {
    return {
      visible: false,
      batchFeeCycleInfo: {
        cycles: '',
        tempCycle: '',
        custEndTime: '',
        receivedAmount: '',
        fee: {}
      }
    }
  },
  methods: {
    open(fee) {
      this.batchFeeCycleInfo = fee
      this.visible = true
    },

    handleClose() {
      this.visible = false
    },

    changeTempCycle() {
      const tempCycle = this.batchFeeCycleInfo.tempCycle + ""
      if (tempCycle !== '-100') {
        this.batchFeeCycleInfo.cycles = "1"
      }
    },

    _doSubmitFeeCycle() {

      this.$emit('changeMonth', this.batchFeeCycleInfo)
      this.handleClose()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog__body {
  padding: 20px;
}
</style>