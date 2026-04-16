<template>
  <div>
    <el-table :data="feeDetails" border style="width: 100%">
      <el-table-column prop="feeTypeCdName" :label="$t('feeConfigDetail.feeType')" align="center"></el-table-column>
      <el-table-column prop="feeName" :label="$t('feeConfigDetail.feeItem')" align="center"></el-table-column>
      <el-table-column prop="feeFlagName" :label="$t('feeConfigDetail.feeFlag')" align="center"></el-table-column>
      <el-table-column prop="billTypeName" :label="$t('feeConfigDetail.billType')" align="center"></el-table-column>
      <el-table-column :label="$t('feeConfigDetail.paymentType')" align="center">
        <template slot-scope="scope">
          {{ scope.row.paymentCd == '1200' ? $t('feeConfigDetail.prePayment') : $t('feeConfigDetail.postPayment') }}
        </template>
      </el-table-column>
      <el-table-column prop="paymentCycle" :label="$t('feeConfigDetail.paymentCycle')" align="center"></el-table-column>
      <el-table-column :label="$t('feeConfigDetail.validity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}<br>{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetail.squarePrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula == '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column prop="additionalAmount" :label="$t('feeConfigDetail.additionalAmount')"
        align="center"></el-table-column>
      <el-table-column :label="$t('feeConfigDetail.deductFrom')" align="center">
        <template slot-scope="scope">
          {{ scope.row.deductFrom == 'Y' ? $t('common.yes') : $t('common.no') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetail.payOnline')" align="center">
        <template slot-scope="scope">
          {{ scope.row.payOnline == 'Y' ? $t('common.yes') : $t('common.no') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetail.scale')" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.scale == '1'">{{ $t('feeConfigDetail.rounding') }}</div>
          <div v-if="scope.row.scale == '3'">{{ $t('feeConfigDetail.upRound') }}</div>
          <div v-if="scope.row.scale == '4'">{{ $t('feeConfigDetail.downRound') }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="decimalPlace" :label="$t('feeConfigDetail.decimalPlace')" align="center"></el-table-column>
      <el-table-column :label="$t('feeConfigDetail.action')" align="center">
        <template slot-scope="scope">
          {{ getHisConfigOperate(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column prop="userName" :label="$t('feeConfigDetail.operator')" align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('feeConfigDetail.operateTime')" align="center"></el-table-column>
    </el-table>

    <el-pagination class="pagination-wrapper" @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>
  </div>
</template>

<script>
import { queryHisFeeConfig } from '@/api/fee/feeConfigDetailApi'

export default {
  name: 'FeeConfigDetailHis',
  data() {
    return {
      feeDetails: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      queryParams: {
        configId: '',
        staffNameLike: '',
        feeNameLike: '',
        logStartTime: '',
        logEndTime: ''
      }
    }
  },
  methods: {
    switchTab(params) {
      this.queryParams.configId = params.configId
      this.loadData()
    },
    async loadData() {
      try {
        const params = {
          ...this.queryParams,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.getCommunityId()
        }
        const response = await queryHisFeeConfig(params)
        this.feeDetails = response.data
        this.pagination.total = response.total
      } catch (error) {
        console.error('Failed to load fee config history:', error)
      }
    },
    getHisConfigOperate(fee) {
      const feeCount = this.feeDetails.filter(item => item.bId === fee.bId).length
      if (feeCount <= 1) {
        if (fee.operate === 'ADD') return this.$t('feeConfigDetail.add')
        if (fee.operate === 'DEL') return this.$t('feeConfigDetail.delete')
        return '-'
      }
      if (fee.operate === 'ADD') return this.$t('feeConfigDetail.modifyNew')
      if (fee.operate === 'DEL') return this.$t('feeConfigDetail.modifyOld')
      return '-'
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  }
}
</script>

<style scoped>
.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
</style>