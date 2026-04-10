<template>
  <div class="admin-pay-fee-detail-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-admin-community ref="selectCommunity" @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminPayFeeDetail.search.title') }}</span>
          </div>
          <el-form :model="searchForm" label-width="auto">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.startTime')">
                  <el-date-picker v-model="searchForm.startTime" type="datetime"
                    :placeholder="$t('adminPayFeeDetail.search.startTimePlaceholder')" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.endTime')">
                  <el-date-picker v-model="searchForm.endTime" type="datetime"
                    :placeholder="$t('adminPayFeeDetail.search.endTimePlaceholder')" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.paymentMethod')">
                  <el-select v-model="searchForm.primeRate"
                    :placeholder="$t('adminPayFeeDetail.search.paymentMethodPlaceholder')" style="width: 100%">
                    <el-option v-for="item in paymentMethods" :key="item.statusCd" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.feeStatus')">
                  <el-select v-model="searchForm.state" :placeholder="$t('adminPayFeeDetail.search.feeStatusPlaceholder')"
                    style="width: 100%">
                    <el-option v-for="item in feeStatuses" :key="item.statusCd" :label="item.name"
                      :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.feeType')">
                  <el-select v-model="searchForm.feeTypeCd"
                    :placeholder="$t('adminPayFeeDetail.search.feeTypePlaceholder')" style="width: 100%">
                    <el-option v-for="item in feeTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.feeStartTime')">
                  <el-date-picker v-model="searchForm.feeStartTime" type="datetime"
                    :placeholder="$t('adminPayFeeDetail.search.feeStartTimePlaceholder')" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.feeEndTime')">
                  <el-date-picker v-model="searchForm.feeEndTime" type="datetime"
                    :placeholder="$t('adminPayFeeDetail.search.feeEndTimePlaceholder')" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('adminPayFeeDetail.search.payerObjName')">
                  <el-input v-model="searchForm.payerObjName"
                    :placeholder="$t('adminPayFeeDetail.search.payerObjNamePlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" style="text-align: right">
                <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
                <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <el-card style="margin-top: 20px">
          <div slot="header" class="text-left">
            <span>{{ $t('adminPayFeeDetail.list.title') }}</span>
            <el-tooltip :content="$t('adminPayFeeDetail.list.tooltip')" placement="top">
              <i class="el-icon-info" style="margin-left: 10px; cursor: pointer" />
            </el-tooltip>
            <div style="float: right">
              <el-button type="primary" size="small" @click="handleExport">
                {{ $t('common.export') }}
              </el-button>
            </div>
          </div>
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="oId" :label="$t('adminPayFeeDetail.table.orderId')" align="center" />
            <el-table-column :label="$t('adminPayFeeDetail.table.payerInfo')" align="center" width="150">
              <template slot-scope="scope">
                <div>{{ scope.row.payerObjName }}/{{ scope.row.ownerName }}</div>
                <div>{{ scope.row.communityName }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminPayFeeDetail.table.feeInfo')" align="center">
              <template slot-scope="scope">
                {{ scope.row.feeTypeCdName }}>{{ scope.row.feeName }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('adminPayFeeDetail.table.feeStatus')" align="center" />
            <el-table-column prop="primeRate" :label="$t('adminPayFeeDetail.table.paymentMethod')" align="center" />
            <el-table-column :label="$t('adminPayFeeDetail.table.feePeriod')" align="center" width="150">
              <template slot-scope="scope">
                <div>{{ formatDate(scope.row.startTime) }}</div>
                <div>~</div>
                <div>{{ formatDate(scope.row.endTime) }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminPayFeeDetail.table.paymentTime')" align="center" />
            <el-table-column prop="cashierName" :label="$t('adminPayFeeDetail.table.cashier')" align="center" />
            <el-table-column :label="$t('adminPayFeeDetail.table.payableAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.payableAmount }}/{{ scope.row.receivableAmount }}
              </template>
            </el-table-column>
            <el-table-column prop="receivedAmount" :label="$t('adminPayFeeDetail.table.receivedAmount')" align="center" />
            <el-table-column prop="withholdAmount" :label="$t('adminPayFeeDetail.table.withholdAmount')" align="center" />
            <el-table-column :label="$t('adminPayFeeDetail.table.discountAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.preferentialAmount }}/{{ scope.row.deductionAmount }}
              </template>
            </el-table-column>
            <el-table-column prop="giftAmount" :label="$t('adminPayFeeDetail.table.giftAmount')" align="center" />
            <el-table-column prop="lateFee" :label="$t('adminPayFeeDetail.table.lateFee')" align="center" />
            <el-table-column prop="builtUpArea" :label="$t('adminPayFeeDetail.table.area')" align="center" />
            <el-table-column prop="psName" :label="$t('adminPayFeeDetail.table.parkingSpace')" align="center" />
            <el-table-column prop="remark" :label="$t('adminPayFeeDetail.table.remark')" align="center" width="200" />
          </el-table>

          <div style="margin-top: 20px">
            <el-row>
              <el-col :span="2">
                <div>{{ $t('adminPayFeeDetail.summary.subtotal') }}</div>
                <div>{{ $t('adminPayFeeDetail.summary.total') }}</div>
              </el-col>
              <el-col :span="2">
                <div>
                  {{ $t('adminPayFeeDetail.summary.receivable') }}: {{ summary.totalReceivableAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
                <div>
                  {{ $t('adminPayFeeDetail.summary.receivable') }}: {{ summary.allReceivableAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
              </el-col>
              <el-col :span="2">
                <div>
                  {{ $t('adminPayFeeDetail.summary.received') }}: {{ summary.totalReceivedAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
                <div>
                  {{ $t('adminPayFeeDetail.summary.received') }}: {{ summary.allReceivedAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
              </el-col>
              <el-col :span="2">
                <div>
                  {{ $t('adminPayFeeDetail.summary.discount') }}: {{ summary.totalPreferentialAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
                <div>
                  {{ $t('adminPayFeeDetail.summary.discount') }}: {{ summary.allPreferentialAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
              </el-col>
              <el-col :span="2">
                <div>
                  {{ $t('adminPayFeeDetail.summary.deduction') }}: {{ summary.totalDeductionAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
                <div>
                  {{ $t('adminPayFeeDetail.summary.deduction') }}: {{ summary.allDeductionAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
              </el-col>
              <el-col :span="2">
                <div>
                  {{ $t('adminPayFeeDetail.summary.gift') }}: {{ summary.totalGiftAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
                <div>
                  {{ $t('adminPayFeeDetail.summary.gift') }}: {{ summary.allGiftAmount }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
              </el-col>
              <el-col :span="2">
                <div>
                  {{ $t('adminPayFeeDetail.summary.lateFee') }}: {{ summary.totalLateFee }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
                <div>
                  {{ $t('adminPayFeeDetail.summary.lateFee') }}: {{ summary.allLateFee }}
                  {{ $t('adminPayFeeDetail.summary.yuan') }}
                </div>
              </el-col>
            </el-row>
          </div>

          <div style="margin-top: 20px" class="text-left">
            <el-row>
              <el-col :span="18">
                <div>{{ $t('adminPayFeeDetail.summary.discountDesc') }}</div>
                <div>{{ $t('adminPayFeeDetail.summary.deductionDesc') }}</div>
                <div>{{ $t('adminPayFeeDetail.summary.giftDesc') }}</div>
                <div>{{ $t('adminPayFeeDetail.summary.lateFeeDesc') }}</div>
              </el-col>
              <el-col :span="6">
                <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
                  :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange" @current-change="handleCurrentChange" />
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminPayFeeDetail, exportFeeData } from '@/api/fee/adminPayFeeDetailApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import { getDict } from '@/api/community/communityApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'AdminPayFeeDetailList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      searchForm: {
        communityId: '',
        floorId: '',
        roomNum: '',
        payerObjName: '',
        configId: '',
        primeRate: '',
        state: '',
        feeTypeCd: '',
        startTime: '',
        endTime: '',
        feeStartTime: '',
        feeEndTime: '',
        page: 1,
        row: 10
      },
      tableData: [],
      paymentMethods: [],
      feeStatuses: [],
      feeTypes: [],
      summary: {
        totalReceivableAmount: '0.00',
        totalReceivedAmount: '0.00',
        allReceivableAmount: '0.00',
        allReceivedAmount: '0.00',
        totalPreferentialAmount: '0.00',
        totalDeductionAmount: '0.00',
        totalLateFee: '0.00',
        totalVacantHousingDiscount: '0.00',
        totalVacantHousingReduction: '0.00',
        totalNoDeduction: '0.00',
        totalCashDeduction: '0.00',
        totalPointDeduction: '0.00',
        totalDiscountCouponDeduction: '0.00',
        allPreferentialAmount: '0.00',
        allDeductionAmount: '0.00',
        allLateFee: '0.00',
        allVacantHousingDiscount: '0.00',
        allVacantHousingReduction: '0.00',
        allGiftAmount: '0.00',
        allNoDeduction: '0.00',
        allCashDeduction: '0.00',
        allPointDeduction: '0.00',
        allDiscountCouponDeduction: '0.00',
        totalGiftAmount: '0.00'
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadDictData()
    this.fetchData()
  },
  methods: {
    async loadDictData() {
      this.paymentMethods = await getDict('pay_fee_detail', 'prime_rate')
      this.feeStatuses = await getDict('pay_fee_detail', 'state')
      this.feeTypes = await getDict('pay_fee_config', 'fee_type_cd')
    },
    async fetchData() {
      try {
        const params = { ...this.searchForm }
        const { data, total, sumTotal } = await queryAdminPayFeeDetail(params)

        this.tableData = data.map(item => ({
          ...item,
          lateFee: (item.lateFee * -1).toFixed(2)
        }))

        this.pagination.total = total
        this.updateSummary(sumTotal)
      } catch (error) {
        //this.$message.error(this.$t('adminPayFeeDetail.fetchError'))
      }
    },
    updateSummary(sumTotal) {
      const defaultZero = '0.00'

      this.summary = {
        totalReceivableAmount: sumTotal.totalReceivableAmount || defaultZero,
        totalReceivedAmount: sumTotal.totalReceivedAmount || defaultZero,
        allReceivableAmount: sumTotal.allReceivableAmount || defaultZero,
        allReceivedAmount: sumTotal.allReceivedAmount || defaultZero,
        totalPreferentialAmount: sumTotal.totalPreferentialAmount || defaultZero,
        totalDeductionAmount: sumTotal.totalDeductionAmount || defaultZero,
        totalLateFee: sumTotal.totalLateFee ? (sumTotal.totalLateFee * -1).toFixed(2) : defaultZero,
        allPreferentialAmount: sumTotal.allPreferentialAmount || defaultZero,
        allDeductionAmount: sumTotal.allDeductionAmount || defaultZero,
        allLateFee: sumTotal.allLateFee  ? (sumTotal.allLateFee * -1).toFixed(2) : defaultZero,
        allGiftAmount: sumTotal.allGiftAmount || defaultZero,
        totalGiftAmount: sumTotal.totalGiftAmount || defaultZero
      }
    },
    handleSearch() {
      this.searchForm.page = 1
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        communityId: this.searchForm.communityId,
        floorId: '',
        roomNum: '',
        payerObjName: '',
        configId: '',
        primeRate: '',
        state: '',
        feeTypeCd: '',
        startTime: '',
        endTime: '',
        feeStartTime: '',
        feeEndTime: '',
        page: 1,
        row: this.searchForm.row
      }
      this.fetchData()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.fetchData()
    },
    handleSizeChange(size) {
      this.searchForm.row = size
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.searchForm.page = page
      this.fetchData()
    },
    async handleExport() {
      try {
        const params = {
          ...this.searchForm,
          pagePath: 'adminPayFeeDetail'
        }
        await exportFeeData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        // 这里可以添加跳转到下载页面的逻辑
        // this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        this.$message.error(this.$t('adminPayFeeDetail.exportError'))
      }
    },
    formatDate(date) {
      if (!date) return '-'
      return dateFormat(date)
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-pay-fee-detail-container {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;

    .el-card__header {
      font-weight: bold;
      background-color: #f5f7fa;
    }
  }

  .el-table {
    margin-top: 20px;

    .cell {
      white-space: pre-line;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }

  .summary-row {
    margin-top: 20px;
    padding: 10px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .summary-item {
      margin-right: 20px;
    }
  }
}
</style>