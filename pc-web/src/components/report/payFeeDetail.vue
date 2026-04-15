<template>
  <div class="pay-fee-detail-container">

    <el-form :inline="true" class="search-form text-left">
      <el-form-item>
        <el-date-picker v-model="payFeeDetailInfo.conditions.startTime"
          :placeholder="$t('reportPayFeeDetail.selectStartTime')" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" clearable/>
      </el-form-item>
      <el-form-item>
        <el-date-picker v-model="payFeeDetailInfo.conditions.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('reportPayFeeDetail.selectEndTime')"
           clearable/>
      </el-form-item>
      <el-form-item>
        <el-select v-model="payFeeDetailInfo.conditions.primeRate"
          :placeholder="$t('reportPayFeeDetail.selectPrimeRate')" style="width: 100%;" clearable>
          <el-option v-for="(item, index) in payFeeDetailInfo.primeRates" :key="index" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="payFeeDetailInfo.conditions.state" :placeholder="$t('reportPayFeeDetail.selectState')"
          style="width: 100%;" clearable>
          <el-option v-for="(item, index) in payFeeDetailInfo.states" :key="index" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="payFeeDetailInfo.conditions.payerObjName"
          :placeholder="$t('reportPayFeeDetail.payerObjName')" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="payFeeDetailInfo.conditions.feeTypeCd" :placeholder="$t('reportPayFeeDetail.selectFeeType')"
          style="width: 100%;" @change="_selectConfig" clearable>
          <el-option v-for="(item, index) in payFeeDetailInfo.feeTypeCds" :key="index" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="_queryDetailMethod">{{ $t('reportPayFeeDetail.search') }}</el-button>
        <el-button type="info" @click="_resetDetailMethod" class="reset-btn">{{ $t('reportPayFeeDetail.reset')
          }}</el-button>
        <el-button type="primary" size="small" @click="_exportPayFeeDetail">{{ $t('reportPayFeeDetail.export')
          }}</el-button>
      </el-form-item>
    </el-form>


    <el-form :inline="true" class="extra-form text-left">

      <el-form-item>
        <el-select v-model="payFeeDetailInfo.conditions.configId" :placeholder="$t('reportPayFeeDetail.selectFeeItem')"
          style="width: 100%;" clearable>
          <el-option v-for="(item, index) in payFeeDetailInfo.feeConfigDtos" :key="index" :label="item.feeName"
            :value="item.configId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-date-picker v-model="payFeeDetailInfo.conditions.feeStartTime"
          :placeholder="$t('reportPayFeeDetail.selectFeeStartTime')" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" class="feeStartTime" clearable></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-date-picker v-model="payFeeDetailInfo.conditions.feeEndTime"
          :placeholder="$t('reportPayFeeDetail.selectFeeEndTime')" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" class="feeEndTime" clearable></el-date-picker>
      </el-form-item>
    </el-form>


    <el-row :gutter="20">
      <el-col :span="24">
        <el-table :data="payFeeDetailInfo.fees" border style="width: 100%">
          <el-table-column :label="$t('reportPayFeeDetail.orderId')" align="center">
            <template slot-scope="scope">
              {{ scope.row.oId ? scope.row.oId : '-' }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.roomOwner')" align="center">
            <template slot-scope="scope">
              <span style="width: 110px;">{{ scope.row.payerObjName }}/{{ scope.row.ownerName }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.feeTypeFeeItem')" align="center">
            <template slot-scope="scope">
              {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
            </template>
          </el-table-column>
          <el-table-column prop="stateName" :label="$t('reportPayFeeDetail.feeState')" align="center"></el-table-column>
          <el-table-column prop="primeRate" :label="$t('reportPayFeeDetail.payWay')" align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.feeTimePeriod')" align="center">
            <template slot-scope="scope">
              <span style="width: 110px;">{{ vcDateFormat(scope.row.startTime) }}<br>~<br><span
                  v-if="scope.row.startTime >= scope.row.endTime">{{ vcDateFormat(scope.row.endTime) }}</span><span
                  v-else>{{ vcDateFormat(scope.row.endTime) }}</span></span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('reportPayFeeDetail.payTime')" align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.cashier')" align="center">
            <template slot-scope="scope">
              {{ scope.row.cashierName || '-' }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.payableReceivableAmount')" align="center">
            <template slot-scope="scope">
              {{ scope.row.payableAmount }}/{{ scope.row.receivableAmount }}
            </template>
          </el-table-column>
          <el-table-column prop="receivedAmount" :label="$t('reportPayFeeDetail.receivedAmount')"
            align="center"></el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.accountDeduction')" align="center">
            <template slot-scope="scope">
              {{ scope.row.withholdAmount || 0 }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('reportPayFeeDetail.discountDeduction')" align="center">
            <template slot-scope="scope">
              {{ scope.row.preferentialAmount }}/{{ scope.row.deductionAmount }}
            </template>
          </el-table-column>
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
            <el-col :span="2">{{ $t('reportPayFeeDetail.receivable') }}: {{ payFeeDetailInfo.totalReceivableAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.received') }}: {{ payFeeDetailInfo.totalReceivedAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.discount') }}: {{ payFeeDetailInfo.totalPreferentialAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.deduction') }}: {{ payFeeDetailInfo.totalDeductionAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.giftAmount') }}: {{ payFeeDetailInfo.totalGiftAmount }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.lateFee') }}: {{ payFeeDetailInfo.totalLateFee }}</el-col>
          </el-row>
          <el-row>
            <el-col :span="1">{{ $t('reportPayFeeDetail.total') }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.receivable') }}: {{ payFeeDetailInfo.allReceivableAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.received') }}: {{ payFeeDetailInfo.allReceivedAmount }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.discount') }}: {{ payFeeDetailInfo.allPreferentialAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.deduction') }}: {{ payFeeDetailInfo.allDeductionAmount
              }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.giftAmount') }}: {{ payFeeDetailInfo.allGiftAmount }}</el-col>
            <el-col :span="2">{{ $t('reportPayFeeDetail.lateFee') }}: {{ payFeeDetailInfo.allLateFee }}</el-col>
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
import { queryPayFeeDetail, exportPayFeeDetail, listFeeConfigs } from '@/api/report/reportPayFeeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'PayFeeDetail',
  data() {
    return {
      payFeeDetailInfo: {
        fees: [],
        total: 0,
        records: 1,
        primeRates: [],
        states: [],
        feeTypeCds: [],
        feeConfigDtos: [],
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
          feeTypeCd: '',
          configId: '',
          startTime: '',
          endTime: '',
          feeStartTime: '',
          feeEndTime: ''
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
      this.payFeeDetailInfo.conditions = conditions
      await this.getDictData()
      await this._listPayFeeDetails()
    },
    async getDictData() {
      try {
        const primeRates = await getDict('pay_fee_detail', 'prime_rate')
        this.payFeeDetailInfo.primeRates = primeRates
        const states = await getDict('pay_fee_detail', 'state')
        this.payFeeDetailInfo.states = states
        const feeTypeCds = await getDict('pay_fee_config', 'fee_type_cd')
        this.payFeeDetailInfo.feeTypeCds = feeTypeCds
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _queryDetailMethod() {
      this.page.current = 1
      await this._listPayFeeDetails()
    },
    async _listPayFeeDetails() {
      try {
        this.payFeeDetailInfo.conditions.page = this.page.current
        this.payFeeDetailInfo.conditions.row = this.page.size
        this.payFeeDetailInfo.conditions.communityId = getCommunityId()
        const params = this.payFeeDetailInfo.conditions
        const data = await queryPayFeeDetail(params)
        this.payFeeDetailInfo.fees = data.data || []
        this.page.total = data.total || 0
        this.payFeeDetailInfo.total = data.total || 0
        // 处理汇总数据
        this.payFeeDetailInfo.totalReceivableAmount = data.sumTotal.totalReceivableAmount || 0.0
        this.payFeeDetailInfo.totalReceivedAmount = data.sumTotal.totalReceivedAmount || 0.0
        this.payFeeDetailInfo.allReceivableAmount = data.sumTotal.allReceivableAmount || 0.0
        this.payFeeDetailInfo.allReceivedAmount = data.sumTotal.allReceivedAmount || 0.0
        this.payFeeDetailInfo.totalPreferentialAmount = data.sumTotal.totalPreferentialAmount || 0.0
        this.payFeeDetailInfo.totalDeductionAmount = data.sumTotal.totalDeductionAmount || 0.0
        this.payFeeDetailInfo.totalLateFee = (data.sumTotal.totalLateFee || 0.0) * -1
        this.payFeeDetailInfo.totalGiftAmount = data.sumTotal.totalGiftAmount || 0.0
        this.payFeeDetailInfo.allPreferentialAmount = data.sumTotal.allPreferentialAmount || 0.0
        this.payFeeDetailInfo.allDeductionAmount = data.sumTotal.allDeductionAmount || 0.0
        this.payFeeDetailInfo.allLateFee = (data.sumTotal.allLateFee || 0.0) * -1
        this.payFeeDetailInfo.allGiftAmount = data.sumTotal.allGiftAmount || 0.0
      } catch (error) {
        console.error('请求失败处理', error)
      }
    },
    async _resetDetailMethod() {
      this.payFeeDetailInfo.conditions = {
        payerObjName: '',
        primeRate: '',
        state: '',
        feeTypeCd: '',
        configId: '',
        startTime: '',
        endTime: '',
        feeStartTime: '',
        feeEndTime: ''
      }
      this.payFeeDetailInfo.feeConfigDtos = []
      await this._listPayFeeDetails()
    },
    async _selectConfig() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId(),
          feeTypeCd: this.payFeeDetailInfo.conditions.feeTypeCd,
          isFlag: '0'
        }
        const data = await listFeeConfigs(params)
        this.payFeeDetailInfo.feeConfigDtos = data.feeConfigs || []
      } catch (error) {
        console.error('获取费用项失败', error)
      }
    },
    async _exportPayFeeDetail() {
      try {
        const params = { ...this.payFeeDetailInfo.conditions, pagePath: 'reportPayFeeDetail' }
        const data = await exportPayFeeDetail(params)
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
      this._listPayFeeDetails()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listPayFeeDetails()
    }
  }
}
</script>

<style lang="scss" scoped>
.pay-fee-detail-container {

  .search-form,
  .extra-form {
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