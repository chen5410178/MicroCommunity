<template>
  <div>
    <div class="margin-top">
      <el-table :data="contractDetailHisRoomFeeInfo.feeDetails" style="width: 100%; margin-top: 10px" border stripe>
        <el-table-column prop="feeName" :label="$t('contractDetailHisRoomFee.feeItem')" align="center" />
        <el-table-column prop="payerObjName" :label="$t('contractDetailHisRoomFee.payerObject')" align="center" />
        <el-table-column prop="cycles" :label="$t('contractDetailHisRoomFee.cycle')" align="center" />
        <el-table-column :label="$t('contractDetailHisRoomFee.receivableAmount')" align="center">
          <template #default="{ row }">
            {{ row.receivableAmount }}/{{ row.receivedAmount }}<br>
            <div v-if="row.acctAmount > 0">
              {{ $t('contractDetailHisRoomFee.accountDeduction') }}: {{ row.acctAmount }}<br>
            </div>
            <div v-for="(item, index) in row.payFeeDetailDiscountDtoList" :key="index">
              {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="primeRateName" :label="$t('contractDetailHisRoomFee.paymentMethod')" align="center" />
        <el-table-column :label="$t('contractDetailHisRoomFee.paymentPeriod')" align="center">
          <template #default="{ row }">
            {{ dateFormat(row.startTime) }}~<br>
            {{ dateFormat(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('contractDetailHisRoomFee.paymentTime')" align="center" />
        <el-table-column prop="cashierName" :label="$t('contractDetailHisRoomFee.cashier')" align="center">
          <template #default="{ row }">
            {{ row.cashierName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('contractDetailHisRoomFee.status')" align="center" />
        <el-table-column prop="remark" :label="$t('contractDetailHisRoomFee.remark')" align="center" />
        <el-table-column :label="$t('contractDetailHisRoomFee.operation')" align="center">
          <template #default="{ row }">
            <el-button v-if="row.state == '1400' || row.state == '1200' || row.state == ''" type="text" size="small"
              @click="_toRefundFee(row)">
              {{ $t('contractDetailHisRoomFee.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="pagination" layout="total, sizes, prev, pager, next, jumper"
        :total="contractDetailHisRoomFeeInfo.total" :page-size="pageSize" @current-change="handleCurrentChange"
        @size-change="handleSizeChange" />
    </div>
  </div>
</template>

<script>
import { queryFeeDetail } from '@/api/contract/contractDetailHisRoomFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailHisRoomFee',
  data() {
    return {
      contractDetailHisRoomFeeInfo: {
        total: 0,
        records: 1,
        feeDetails: [],
        ownerId: ''
      },
      pageSize: 10,
      currentPage: 1,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.contractDetailHisRoomFeeInfo.ownerId = data.ownerId
      this._listContractDetailHisRoomFeeDetails()
    },
    dateFormat(date) {
      return this.$moment(date).format('YYYY-MM-DD')
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listContractDetailHisRoomFeeDetails()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listContractDetailHisRoomFeeDetails()
    },
    async _listContractDetailHisRoomFeeDetails() {
      try {
        const params = {
          page: this.currentPage,
          row: this.pageSize,
          communityId: this.communityId,
          ownerId: this.contractDetailHisRoomFeeInfo.ownerId,
          payerObjType: '3333'
        }

        const { data: feeDetails, total, records } = await queryFeeDetail(params)
        this.contractDetailHisRoomFeeInfo.total = total
        this.contractDetailHisRoomFeeInfo.records = records
        this.contractDetailHisRoomFeeInfo.feeDetails = feeDetails
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    clearContractDetailHisRoomFeeInfo() {
      this.contractDetailHisRoomFeeInfo = {
        total: 0,
        records: 1,
        feeDetails: [],
        ownerId: ''
      }
    },
    _toRefundFee(detail) {
      this.$router.push(`/pages/property/propertyFee?feeId=${detail.feeId}`)
    },
    switch(param) {
      this.clearContractDetailHisRoomFeeInfo()
      if (param.ownerId === '') {
        return
      }
      Object.assign(this.contractDetailHisRoomFeeInfo, param)
      this._listContractDetailHisRoomFeeDetails()
    },
    notify() {
      this._listContractDetailHisRoomFeeDetails()
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>