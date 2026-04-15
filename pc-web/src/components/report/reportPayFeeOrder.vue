<template>
  <div class="pay-fee-order-container">
        <el-form :inline="true" class="text-left">
          <el-form-item>
            <el-date-picker v-model="payFeeOrderInfo.conditions.startTime"
              :placeholder="$t('reportPayFeeDetail.selectStartTime')" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" class="startTime" clearable></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-date-picker v-model="payFeeOrderInfo.conditions.endTime" :placeholder="$t('reportPayFeeDetail.selectEndTime')"
              type="datetime" value-format="yyyy-MM-dd HH:mm:ss" class="endTime" clearable></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-select v-model="payFeeOrderInfo.conditions.primeRate"
              :placeholder="$t('reportPayFeeDetail.selectPrimeRate')" style="width: 100%;" clearable>
              <el-option v-for="(item, index) in payFeeOrderInfo.primeRates" :key="index" :label="item.name"
                :value="item.statusCd"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="payFeeOrderInfo.conditions.state" :placeholder="$t('reportPayFeeDetail.selectState')"
              style="width: 100%;" clearable>
              <el-option v-for="(item, index) in payFeeOrderInfo.states" :key="index" :label="item.name"
                :value="item.statusCd"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="payFeeOrderInfo.conditions.payerObjName"
              :placeholder="$t('reportPayFeeDetail.payerObjName')" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="payFeeOrderInfo.conditions.cashierName"
              :placeholder="$t('reportPayFeeDetail.cashierName')" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="_queryMethod">{{ $t('reportPayFeeDetail.search') }}</el-button>
            <el-button type="info" @click="_resetMethod" class="reset-btn">{{ $t('reportPayFeeDetail.reset')
              }}</el-button>
            <el-button type="primary"  @click="_exportPayFeeOrder">{{ $t('reportPayFeeDetail.export')
              }}</el-button>
          </el-form-item>
        </el-form>


    <el-row :gutter="20">
      <el-col :span="24">
        <el-table :data="payFeeOrderInfo.fees" border style="width: 100%">
          <el-table-column prop="payOrderId" :label="$t('reportPayFeeDetail.orderId')" align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.orderType')" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.orderType == '2002'">{{ $t('reportPayFeeDetail.payOrder') }}</span>
              <span v-else>{{ $t('reportPayFeeDetail.preOrder') }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.roomOwner')" align="center">
            <template slot-scope="scope">
              <span style="width: 110px;">{{ scope.row.payerObjName }}/{{ scope.row.ownerName }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.feeItems')" align="center">
            <template slot-scope="scope">
              <div v-for="(detail, index) in scope.row.payFeeOrderDetailDtos" :key="index" style="width: 110px;">{{
                detail.feeName }}</div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.feeTimePeriod')" align="center">
            <template slot-scope="scope">
              <div v-for="(detail, index) in scope.row.payFeeOrderDetailDtos" :key="index" style="width: 180px;">{{
                vcDateFormat(detail.startTime) }}~{{ vcDateFormat(detail.endTime) }}</div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.detailReceivableReceived')" align="center">
            <template slot-scope="scope">
              <div v-for="(detail, index) in scope.row.payFeeOrderDetailDtos" :key="index" style="width: 100px;">{{
                detail.receivableAmount }}/{{ detail.receivedAmount }}</div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.orderReceivableReceived')" align="center">
            <template slot-scope="scope">
              {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}
            </template>
          </el-table-column>
          <el-table-column prop="stateName" :label="$t('reportPayFeeDetail.feeState')" align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.payWay')" align="center">
            <template slot-scope="scope">
              {{ scope.row.primeRateName }}
              <a v-if="scope.row.primeFile" :href="scope.row.primeFile" target="_blank">{{
                $t('reportPayFeeDetail.attachment') }}</a>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('reportPayFeeDetail.payTime')" align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.cashier')" align="center">
            <template slot-scope="scope">
              <span style="width: 80px;">{{ scope.row.cashierName || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.accountDeduction')" align="center">
            <template slot-scope="scope">
              {{ scope.row.withholdAmount || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="preferentialAmount" :label="$t('reportPayFeeDetail.discount')"
            align="center"></el-table-column>
          <el-table-column prop="giftAmount" :label="$t('reportPayFeeDetail.giftAmount')"
            align="center"></el-table-column>
          <el-table-column prop="lateFee" :label="$t('reportPayFeeDetail.lateFee')" align="center"></el-table-column>
          <el-table-column prop="builtUpArea" :label="$t('reportPayFeeDetail.area')" align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.parking')" align="center">
            <template slot-scope="scope">
              {{ scope.row.psName || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="remark" :label="$t('reportPayFeeDetail.remark')" align="center"
            style="max-width: 200px;"></el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <div class="summary">
          <el-row>
            <el-col :span="1">{{ $t('reportPayFeeDetail.subtotal') }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.receivable') }}: {{ payFeeOrderInfo.totalReceivableAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.received') }}: {{ payFeeOrderInfo.totalReceivedAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.discount') }}: {{ payFeeOrderInfo.totalPreferentialAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.deduction') }}: {{ payFeeOrderInfo.totalDeductionAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.giftAmount') }}: {{ payFeeOrderInfo.totalGiftAmount }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.lateFee') }}: {{ payFeeOrderInfo.totalLateFee }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="1">{{ $t('reportPayFeeDetail.total') }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.receivable') }}: {{ payFeeOrderInfo.allReceivableAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.received') }}: {{ payFeeOrderInfo.allReceivedAmount }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.discount') }}: {{ payFeeOrderInfo.allPreferentialAmount
              }}</el-col>
              <el-col :span="2">{{ $t('reportPayFeeDetail.deduction') }}: {{ payFeeOrderInfo.allDeductionAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.giftAmount') }}: {{ payFeeOrderInfo.allGiftAmount }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.lateFee') }}: {{ payFeeOrderInfo.allLateFee }}</el-col>
          </el-row>
        </div>
        <div class="notes text-left">
          <div>{{ $t('reportPayFeeDetail.discountNote') }}</div>
          <div>{{ $t('reportPayFeeDetail.deductionNote') }}</div>
          <div>{{ $t('reportPayFeeDetail.giftNote') }}</div>
          <div>{{ $t('reportPayFeeDetail.lateFeeNote') }}</div>
        </div>
        <el-pagination :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
          :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange">
        </el-pagination>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listPayFeeOrder, exportPayFeeOrder } from '@/api/report/reportPayFeeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'PayFeeOrder',
  data() {
    return {
      payFeeOrderInfo: {
        fees: [],
        total: 0,
        records: 1,
        moreCondition: false,
        primeRates: [],
        states: [],
        totalReceivableAmount: 0.0,
        totalReceivedAmount: 0.0,
        allReceivableAmount: 0.0,
        allReceivedAmount: 0.0,
        totalPreferentialAmount: 0.0,
        totalDeductionAmount: 0.0,
        totalLateFee: 0.0,
        totalGiftAmount: 0.0,
        allPreferentialAmount: 0.0,
        allDeductionAmount: 0.0,
        allLateFee: 0.0,
        allGiftAmount: 0.0,
        conditions: {
          payerObjName: '',
          primeRate: '',
          state: '',
          startTime: '',
          endTime: '',
          cashierName: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  mounted() {
  },
  methods: {
    async open(conditions) {
      this.payFeeOrderInfo.conditions = conditions
      await this.getDictData()
      await this._listPayFeeOrders()
    },
    async getDictData() {
      try {
        const primeRates = await getDict('pay_fee_detail', 'prime_rate')
        this.payFeeOrderInfo.primeRates = primeRates
        const states = await getDict('pay_fee_detail', 'state')
        this.payFeeOrderInfo.states = states
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _queryMethod() {
      this.page.current = 1
      await this._listPayFeeOrders()
    },
    async _listPayFeeOrders() {
      try {
        this.payFeeOrderInfo.conditions.page = this.page.current
        this.payFeeOrderInfo.conditions.row = this.page.size
        this.payFeeOrderInfo.conditions.communityId = getCommunityId()
        const params = this.payFeeOrderInfo.conditions
        const data = await listPayFeeOrder(params)
        this.payFeeOrderInfo.fees = data.data || []
        this.page.total = data.total || 0
        this.payFeeOrderInfo.total = data.total || 0
        // 处理汇总数据
        this.payFeeOrderInfo.totalReceivableAmount = data.sumTotal.totalReceivableAmount || 0.0
        this.payFeeOrderInfo.totalReceivedAmount = data.sumTotal.totalReceivedAmount || 0.0
        this.payFeeOrderInfo.allReceivableAmount = data.sumTotal.allReceivableAmount || 0.0
        this.payFeeOrderInfo.allReceivedAmount = data.sumTotal.allReceivedAmount || 0.0
        this.payFeeOrderInfo.totalPreferentialAmount = data.sumTotal.totalPreferentialAmount || 0.0
        this.payFeeOrderInfo.totalDeductionAmount = data.sumTotal.totalDeductionAmount || 0.0
        this.payFeeOrderInfo.totalLateFee = (data.sumTotal.totalLateFee || 0.0) * -1
        this.payFeeOrderInfo.totalGiftAmount = data.sumTotal.totalGiftAmount || 0.0
        this.payFeeOrderInfo.allPreferentialAmount = data.sumTotal.allPreferentialAmount || 0.0
        this.payFeeOrderInfo.allDeductionAmount = data.sumTotal.allDeductionAmount || 0.0
        this.payFeeOrderInfo.allLateFee = (data.sumTotal.allLateFee || 0.0) * -1
        this.payFeeOrderInfo.allGiftAmount = data.sumTotal.allGiftAmount || 0.0
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    async _resetMethod() {
      this.payFeeOrderInfo.conditions = {
        payerObjName: '',
        primeRate: '',
        state: '',
        startTime: '',
        endTime: '',
        cashierName: ''
      }
      await this._listPayFeeOrders()
    },
    async _exportPayFeeOrder() {
      try {
        const params = { ...this.payFeeOrderInfo.conditions, pagePath: 'reportPayFeeDetail' }
        const data = await exportPayFeeOrder(params)
        this.$message.success(data.msg)
        if (data.code === 0) {
          this.$router.push('/#/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败', error)
      }
    },
    vcDateFormat(date) {
      return date ? new Date(date).toLocaleDateString() : ''
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listPayFeeOrders()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listPayFeeOrders()
    }
  }
}
</script>

<style lang="scss" scoped>
.pay-fee-order-container {
  .search-form {
    .el-form-item {
      margin-bottom: 10px;
    }

    .reset-btn {
      margin-left: 10px;
    }
  }

  .summary {
    margin-top: 20px;
  text-align: left;
    font-size: 13px;
    color: #606266;
    .el-row {
      .el-col {
        padding: 5px;
      }
    }
  }

  .notes {
    margin-top: 10px;
  text-align: left;
    font-size: 13px;
    color: #606266;
    div {
      margin-bottom: 5px;
    }
  }
}
</style>