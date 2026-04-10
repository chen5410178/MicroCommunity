<template>
  <div class="report-pay-fee-container">
    <el-table :data="reportHuaningPayFeeInfo.fees" border style="width: 100%" class="margin-top">
      <el-table-column prop="floorNum" :label="$t('reportHuaningPayFee.table.floorNum')" align="center" />
      <el-table-column prop="receivableAmount" :label="$t('reportHuaningPayFee.table.monthlyReceivable')"
        align="center" />
      <el-table-column prop="receivedAmount"
        :label="`${reportHuaningPayFeeInfo.conditions.month}${$t('reportHuaningPayFee.table.monthReceived')}`"
        align="center" />
      <el-table-column prop="receivedAmount1" :label="`${reportHuaningPayFeeInfo.conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart1', {
        year: reportHuaningPayFeeInfo.conditions.year,
        month: reportHuaningPayFeeInfo.conditions.month - 1
      })}`" align="center" />
      <el-table-column prop="receivedAmount2" :label="`${reportHuaningPayFeeInfo.conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart2', {
        year: reportHuaningPayFeeInfo.conditions.year,
        month: reportHuaningPayFeeInfo.conditions.month
      })}`" align="center" />
      <el-table-column prop="receivedAmount3" :label="`${reportHuaningPayFeeInfo.conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart3', {
        nextMonth: _getNextMonth()
      })}`" align="center" />
      <el-table-column prop="receivedAmount4" :label="`${reportHuaningPayFeeInfo.conditions.month}${$t('reportHuaningPayFee.table.monthReceivedPart4', {
        year: reportHuaningPayFeeInfo.conditions.year
      })}`" align="center" />
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
      layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
  </div>
</template>

<script>
import { queryHuaningPayFee } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import {getDateYYYYMMDD} from '@/utils/dateUtil'

export default {
  name: 'ReportHuaningPayFee',
  data() {
    return {
      reportHuaningPayFeeInfo: {
        fees: [],
        conditions: {
          year: new Date().getFullYear(),
          month: new Date().getMonth() + 1
        },
        dateStr:getDateYYYYMMDD()
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    async initData(conditions) {
      this.reportHuaningPayFeeInfo.conditions = { ...conditions }
      this.pagination.current = 1
      await this._listReportHuaningPayFee()
    },
    async _listReportHuaningPayFee() {
      try {
        const params = {
          ...this.reportHuaningPayFeeInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId()
        }

        const { data, total } = await queryHuaningPayFee(params)

        this.reportHuaningPayFeeInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取缴费数据失败:', error)
      }
    },
    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this._listReportHuaningPayFee()
    },
    _getNextMonth() {
      const date = new Date()
      date.setMonth(date.getMonth() + 1)
      return `${date.getFullYear()}${this.$t('common.year')}${date.getMonth() + 1}${this.$t('common.month')}`
    }
  }
}
</script>

<style lang="scss" scoped>
.report-pay-fee-container {
  .margin-top {
    margin-top: 20px;
  }
}
</style>