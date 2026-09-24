<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="20"></el-col>
      <el-col :span="4" class="text-right"></el-col>
    </el-row>

    <el-table :data="simplifyRefundDepositInfo.fees" border style="width: 100%; margin-top:10px">
      <el-table-column prop="payerObjName" :label="$t('simplifyRefundDeposit.chargeObject')"
        align="center"></el-table-column>
      <el-table-column prop="feeName" :label="$t('simplifyRefundDeposit.feeItem')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyRefundDeposit.timePeriod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}~{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="receivedAmount" :label="$t('simplifyRefundDeposit.amount')"
        align="center"></el-table-column>
      <el-table-column prop="createTime" :label="$t('simplifyRefundDeposit.paymentTime')"
        align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyRefundDeposit.status')" align="center"></el-table-column>
      <el-table-column :label="$t('simplifyRefundDeposit.operation')" align="center">
        <template slot-scope="scope">
          <el-button-group>
            <el-button v-if="scope.row.state == '1400'" size="mini" @click="_openRefundModel(scope.row)">
              {{ $t('simplifyRefundDeposit.refundDeposit') }}
            </el-button>
            <el-button size="mini" @click="_toSimplifyFeeDepositFeeDetail(scope.row)">
              {{ $t('simplifyRefundDeposit.detail') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top">
      <el-col :span="24" class="text-right">
        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
          layout="total, prev, pager, next" :total="simplifyRefundDepositInfo.total"></el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryFeeDeposit } from '@/api/fee/simplifyRefundDepositApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SimplifyRefundDeposit',
  data() {
    return {
      simplifyRefundDepositInfo: {
        fees: [],
        ownerId: '',
        roomId: '',
        roomName: '',
        total: 0,
        records: 0
      },
      currentPage: 1,
      pageSize: 10,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    handleCurrentChange(val) {
      this.currentPage = val
      this._listSimplifyFeeDeposit(val, this.pageSize)
    },
    _listSimplifyFeeDeposit(page, rows) {
      const params = {
        page,
        row: rows,
        payerObjId: this.simplifyRefundDepositInfo.roomId,
        ownerId: this.simplifyRefundDepositInfo.ownerId,
        communityId: this.communityId,
        state: '1400'
      }

      queryFeeDeposit(params).then(response => {
        if (response.code !== 0) return
        this.simplifyRefundDepositInfo.total = response.total
        this.simplifyRefundDepositInfo.records = response.records
        this.simplifyRefundDepositInfo.fees = response.data
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    clearSimplifyFeeDepositInfo() {
      this.simplifyRefundDepositInfo = {
        fees: [],
        ownerId: '',
        roomId: '',
        roomName: '',
        total: 0,
        records: 0
      }
    },
    _openRefundModel() {
      const roomId = this.simplifyRefundDepositInfo.roomId
      this.$router.push(`/views/fee/refundDepositFee?roomId=${roomId}`)
    },
    _toSimplifyFeeDepositFeeDetail(fee) {
      this.$router.push(`/views/fee/propertyFee?feeId=${fee.feeId}`)
    },
    switchData(param) {
      if (!param.roomId) return
      this.clearSimplifyFeeDepositInfo()
      Object.assign(this.simplifyRefundDepositInfo, param)
      this._listSimplifyFeeDeposit(1, 10)
    },
    open(params) {
      this.switchData(params)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}

.text-right {
  text-align: right;
}
</style>