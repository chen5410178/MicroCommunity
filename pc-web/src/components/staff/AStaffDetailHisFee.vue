<template>
    <div class="staff-his-fee-container">
      <el-table :data="aStaffDetailHisFeeInfo.feeDetails" border style="width: 100%" class="margin-top">
        <el-table-column prop="feeName" :label="$t('staffDetailHisFee.feeName')" align="center" />
        <el-table-column prop="payerObjName" :label="$t('staffDetailHisFee.payerObjName')" align="center" />
        <el-table-column prop="cycles" :label="$t('staffDetailHisFee.cycles')" align="center" />
        <el-table-column :label="$t('staffDetailHisFee.amount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.receivableAmount }}/{{ scope.row.receivedAmount }}
            <div v-if="scope.row.acctAmount > 0">
              {{ $t('staffDetailHisFee.acctAmount') }}: {{ scope.row.acctAmount }}
            </div>
            <div v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
              {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="primeRateName" :label="$t('staffDetailHisFee.primeRateName')" align="center" />
        <el-table-column :label="$t('staffDetailHisFee.feePeriod')" align="center">
          <template slot-scope="scope">
            {{ formatDate(scope.row.startTime) }}~{{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('staffDetailHisFee.createTime')" align="center" />
        <el-table-column :label="$t('staffDetailHisFee.cashierName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.cashierName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('staffDetailHisFee.stateName')" align="center" />
        <el-table-column prop="remark" :label="$t('staffDetailHisFee.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="['1400', '1200', ''].includes(scope.row.state)"
              size="mini"
              type="primary"
              @click="toRefundFee(scope.row)"
            >
              {{ $t('common.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 15, 20, 30]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </template>
  
  <script>
  import { queryAdminFeeDetail } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailHisFee',
    data() {
      return {
        aStaffDetailHisFeeInfo: {
          feeDetails: [],
          staffId: '',
          state: ''
        },
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailHisFeeInfo.staffId = data.staffId
        this.loadHisFeeData(1, this.page.size)
      },
      async loadHisFeeData(page, row) {
        try {
          const { data, total } = await queryAdminFeeDetail({
            cashierId: this.aStaffDetailHisFeeInfo.staffId,
            page,
            row
          })
          this.aStaffDetailHisFeeInfo.feeDetails = data.feeDetails
          this.page.total = total
        } catch (error) {
          this.$message.error(this.$t('staffDetailHisFee.fetchError'))
        }
      },
      toRefundFee(detail) {
        this.$router.push(`/views/fee/adminFeeDetail?feeId=${detail.feeId}`)
      },
      formatDate(date) {
        return date // Implement date formatting if needed
      },
      handleSizeChange(val) {
        this.page.size = val
        this.loadHisFeeData(this.page.current, this.page.size)
      },
      handleCurrentChange(val) {
        this.page.current = val
        this.loadHisFeeData(this.page.current, this.page.size)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-his-fee-container {
    padding: 20px;
    .margin-top {
      margin-top: 20px;
    }
  }
  </style>