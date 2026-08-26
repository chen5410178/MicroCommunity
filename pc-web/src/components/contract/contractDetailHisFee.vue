<template>
  <div class="margin-top">
    <el-table :data="contractDetailHisFeeInfo.feeDetails" style="width: 100%" border stripe>
      <el-table-column prop="feeName" :label="$t('contractDetailHisFee.feeItem')" align="center"></el-table-column>
      <el-table-column prop="payerObjName" :label="$t('contractDetailHisFee.payer')" align="center"></el-table-column>
      <el-table-column prop="cycles" :label="$t('contractDetailHisFee.cycle')" align="center"></el-table-column>
      <el-table-column :label="$t('contractDetailHisFee.receivableAmount')" align="center">
        <template slot-scope="scope">
          {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}<br>
          <div v-if="scope.row.acctAmount > 0">
            {{ $t('contractDetailHisFee.accountDeduction') }}: {{ scope.row.acctAmount }}<br>
          </div>
          <div v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
            {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}<br>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="primeRateName" :label="$t('contractDetailHisFee.paymentMethod')"
        align="center"></el-table-column>
      <el-table-column :label="$t('contractDetailHisFee.paymentPeriod')" align="center">
        <template slot-scope="scope">
          {{ $options.filters.dateFormat(scope.row.startTime) }}~<br />
          {{ $options.filters.dateFormat(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('contractDetailHisFee.paymentTime')"
        align="center"></el-table-column>
      <el-table-column prop="cashierName" :label="$t('contractDetailHisFee.cashier')" align="center">
        <template slot-scope="scope">
          {{ scope.row.cashierName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('contractDetailHisFee.status')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('contractDetailHisFee.remark')" align="center"></el-table-column>
      <el-table-column :label="$t('contractDetailHisFee.operation')" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.state == '1400' || scope.row.state == '1200' || scope.row.state == ''" size="mini"
            @click="_toRefundFee(scope.row)">
            {{ $t('contractDetailHisFee.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @current-change="handlePageChange" :current-page="pagination.currentPage"
      :page-size="pagination.pageSize" layout="total, prev, pager, next" :total="pagination.total"
      class="margin-top"></el-pagination>
  </div>
</template>

<script>
import { queryFeeDetail } from '@/api/contract/contractDetailHisFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailHisFee',
  data() {
    return {
      contractDetailHisFeeInfo: {
        total: 0,
        records: 1,
        feeDetails: [],
        contractId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(data) {
      this.contractDetailHisFeeInfo.contractId = data.contractId
      this._listContractDetailHisFeeDetails(1, this.pagination.pageSize)
    },
    async _listContractDetailHisFeeDetails(page, row) {
      try {
        const params = {
          page: page || 1,
          row: row || 10,
          communityId: this.communityId,
          payerObjId: this.contractDetailHisFeeInfo.contractId,
        }

        const { data: feeDetails, total, records } = await queryFeeDetail(params)
        this.contractDetailHisFeeInfo.total = total
        this.contractDetailHisFeeInfo.records = records
        this.contractDetailHisFeeInfo.feeDetails = feeDetails
        this.pagination.total = total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    clearContractDetailHisFeeInfo() {
      this.contractDetailHisFeeInfo = {
        total: 0,
        records: 1,
        feeDetails: [],
        contractId: ''
      }
    },
    _toRefundFee(detail) {
      this.$router.push(`/property/propertyFee?feeId=${detail.feeId}`)
    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this._listContractDetailHisFeeDetails(currentPage, this.pagination.pageSize)
    }
  },
  created() {
    this.communityId = getCommunityId()
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>