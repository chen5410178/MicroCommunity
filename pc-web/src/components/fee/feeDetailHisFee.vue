<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="feeDetailHisFeeInfo.feeDetails" style="width: 100%">
        <el-table-column prop="feeName" :label="$t('feeDetailHisFee.feeName')" align="center"></el-table-column>
        <el-table-column prop="payerObjName" :label="$t('feeDetailHisFee.payerObjName')"
          align="center"></el-table-column>
        <el-table-column prop="cycles" :label="$t('feeDetailHisFee.cycles')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailHisFee.amount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}<br>
            <div v-if="scope.row.acctAmount > 0">
              {{ $t('feeDetailHisFee.accountDeduction') }}: {{ scope.row.acctAmount }}<br>
            </div>
            <div v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
              {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="primeRateName" :label="$t('feeDetailHisFee.paymentMethod')"
          align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailHisFee.paymentPeriod')" align="center">
          <template slot-scope="scope">
            {{ dateFormat(scope.row.startTime) }}~<br>
            {{ dateFormat(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('feeDetailHisFee.paymentTime')" align="center"></el-table-column>
        <el-table-column prop="cashierName" :label="$t('feeDetailHisFee.cashier')" align="center">
          <template slot-scope="scope">{{ scope.row.cashierName || '-' }}</template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('feeDetailHisFee.status')" align="center"></el-table-column>
        <el-table-column prop="remark" :label="$t('feeDetailHisFee.remark')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailHisFee.operation')" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state == '1400' || scope.row.state == '1200' || scope.row.state == ''" size="mini"
              @click="_toRefundFee(scope.row)">
              {{ $t('feeDetailHisFee.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next" :total="pagination.total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryFeeDetail } from '@/api/fee/feeDetailHisFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailHisFee',
  data() {
    return {
      feeDetailHisFeeInfo: {
        feeDetails: [],
        feeId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.feeDetailHisFeeInfo.feeId = params.feeId
      this._loadFeeDetailHisFeeData()
    },
    _loadFeeDetailHisFeeData() {
      const params = {
        communityId: getCommunityId(),
        feeId: this.feeDetailHisFeeInfo.feeId,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      queryFeeDetail(params).then(res => {
        this.feeDetailHisFeeInfo.feeDetails = res.feeDetails
        this.pagination.total = res.total
      }).catch(error => {
        console.error('Failed to load fee detail history:', error)
      })
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadFeeDetailHisFeeData()
    },
    _toRefundFee(detail) {
      this.$router.push(`/views/fee/propertyFee?feeId=${detail.feeId}`)
    },
    dateFormat(date) {
      return this.$moment(date).format('YYYY-MM-DD')
    }
  }
}
</script>