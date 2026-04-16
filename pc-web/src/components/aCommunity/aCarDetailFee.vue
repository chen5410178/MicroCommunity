<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="aCarDetailFeeInfo.fees" style="width: 100%">
        <el-table-column prop="feeName" :label="$t('carDetailFee.feeItem')" align="center">
          <template slot-scope="scope">
            <span class="hand" @click="_viewCarFeeConfig(scope.row)">
              {{ scope.row.feeName }}
              <i class="el-icon-info"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('carDetailFee.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('carDetailFee.feeType')" align="center"></el-table-column>
        <el-table-column prop="amountOwed" :label="$t('carDetailFee.amountOwed')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('carDetailFee.createTime')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailFee.duePeriod')" align="center">
          <template slot-scope="scope">
            {{ _getEndTime(scope.row) }}~<br />{{ _getDeadlineTime(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailFee.remark')" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.feeTypeCd == '888800010015' || scope.row.feeTypeCd == '888800010016'">
              <div>{{ $t('carDetailFee.preDegrees') }}：{{ scope.row.preDegrees }}</div>
              <div>{{ $t('carDetailFee.curDegrees') }}：{{ scope.row.curDegrees }}</div>
              <div>{{ $t('carDetailFee.unitPrice') }}：{{ scope.row.mwPrice ? scope.row.mwPrice : scope.row.squarePrice
              }}</div>
              <div>{{ $t('carDetailFee.additionalFee') }}：{{ scope.row.additionalAmount }}</div>
            </div>
            <div v-else>
              <div>{{ $t('carDetailFee.unitPrice') }}：{{ scope.row.squarePrice }}</div>
              <div>{{ $t('carDetailFee.fixedFee') }}：{{ scope.row.additionalAmount }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('carDetailFee.status')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailFee.operation')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12">
          <span>{{ $t('carDetailFee.paymentNotice') }}</span>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-pagination @current-change="handlePageChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminFee } from '@/api/aCommunity/aCarDetailFeeApi'

export default {
  name: 'ACarDetailFee',
  data() {
    return {
      aCarDetailFeeInfo: {
        fees: [],
        carId: '',
        memberId: '',
        carNum: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aCarDetailFeeInfo.carId = data.carId
      this.aCarDetailFeeInfo.carNum = data.carNum
      this.aCarDetailFeeInfo.memberId = data.memberId
      this._loadACarDetailFeeData(1, this.pageSize)
    },
    handleNotify() {
      this._loadACarDetailFeeData(this.currentPage, this.pageSize)
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadACarDetailFeeData(page, this.pageSize)
    },
    async _loadACarDetailFeeData(page, row) {
      try {
        const param = {
          payerObjId: this.aCarDetailFeeInfo.memberId,
          page,
          row
        }
        const response = await listAdminFee(param)
        this.aCarDetailFeeInfo.fees = response.fees
        this.total = response.total
      } catch (error) {
        console.error('Failed to load fee data:', error)
      }
    },
    _getDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return fee.deadlineTime
    },
    _getEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return fee.endTime
    },
    _viewCarFeeConfig(fee) {
      // Implement view fee config logic
      console.log(fee)
    }
  }
}
</script>

<style scoped>
.hand {
  cursor: pointer;
}

.margin-top {
  margin-top: 20px;
}

.text-right {
  text-align: right;
}
</style>