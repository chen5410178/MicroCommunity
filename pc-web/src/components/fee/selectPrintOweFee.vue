<template>
  <el-dialog
    :visible.sync="visible"
    :title="$t('printOweFee.customPaymentTitle')"
    width="800px"
    @close="handleClose"
  >
    <div>
      <el-table :data="fees" border>
        <el-table-column width="50" align="center">
          <template slot="header">
            <el-checkbox v-model="checkAll" @change="handleSelectAll"></el-checkbox>
          </template>
          <template slot-scope="scope">
            <el-checkbox
              :value="selectedFeeMap[scope.$index]"
              @change="handleCheckChange(scope.$index)"
            ></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="payerObjName" :label="$t('printOweFee.chargeObject')" align="center" />
        <el-table-column prop="feeName" :label="$t('printOweFee.itemName')" align="center" />
        <el-table-column :label="$t('printOweFee.feePeriod')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime }} - {{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="oweAmount" :label="$t('printOweFee.amount')" align="center" width="120">
          <template slot-scope="scope">
            {{ scope.row.oweAmount }}
          </template>
        </el-table-column>
      </el-table>
      <div class="margin-top flex justify-between" style="padding: 10px 0; align-items: center;">
        <div>
          <el-checkbox v-model="checkAll" @change="handleSelectAll">
            {{ $t('printOweFee.selectAll') }}
          </el-checkbox>
        </div>
        <div class="flex">
          <span style="margin-right: 20px;">
            {{ $t('printOweFee.selectedCount', { count: selectedCount }) }}
          </span>
          <span>
            {{ $t('printOweFee.selectedAmount') }}：¥{{ selectedTotal.toFixed(2) }}
          </span>
        </div>
      </div>
    </div>
    <span slot="footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'SelectPrintOweFee',
  data() {
    return {
      visible: false,
      fees: [],
      selectedFeeMap: {},
      checkAll: true
    }
  },
  computed: {
    selectedCount() {
      return Object.values(this.selectedFeeMap).filter(Boolean).length
    },
    selectedTotal() {
      let total = 0
      this.fees.forEach((item, index) => {
        if (this.selectedFeeMap[index]) {
          total += parseFloat(item.oweAmount) || 0
        }
      })
      return Math.round(total * 100) / 100
    }
  },
  methods: {
    open(fees) {
      this.fees = fees || []
      this.selectedFeeMap = {}
      this.checkAll = true
      this.fees.forEach((item, index) => {
        this.$set(this.selectedFeeMap, index, true)
      })
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.fees = []
      this.selectedFeeMap = {}
      this.checkAll = true
    },
    handleSelectAll(val) {
      this.checkAll = val
      this.fees.forEach((item, index) => {
        this.$set(this.selectedFeeMap, index, val)
      })
    },
    handleCheckChange(index) {
      this.$set(this.selectedFeeMap, index, !this.selectedFeeMap[index])
      const allChecked = this.fees.every((item, i) => this.selectedFeeMap[i])
      this.checkAll = allChecked
    },
    handleConfirm() {
      const selectedFees = this.fees.filter((item, index) => this.selectedFeeMap[index])
      const totalAmount = selectedFees.reduce((sum, item) => sum + parseFloat(item.oweAmount), 0)
      this.$emit('confirm', {
        selectedFees: selectedFees,
        totalAmount: Math.round(totalAmount * 100) / 100
      })
      this.visible = false
      this.fees = []
      this.selectedFeeMap = {}
      this.checkAll = true
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
</style>
