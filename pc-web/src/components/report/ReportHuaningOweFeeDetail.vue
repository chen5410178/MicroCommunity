<template>
  <div class="report-owe-fee-detail-container">
    <el-table :data="reportHuaningOweFeeDetailInfo.fees" border style="width: 100%" class="margin-top">
      <el-table-column prop="roomName" :label="$t('reportHuaningOweFeeDetail.table.roomNum')" align="center" />
      <el-table-column prop="builtUpArea" :label="$t('reportHuaningOweFeeDetail.table.area')" align="center" />
      <el-table-column prop="feeName" :label="$t('reportHuaningOweFeeDetail.table.fee')" align="center" />
      <el-table-column prop="endTime" :label="$t('reportHuaningOweFeeDetail.table.startTime')" align="center" />
      <el-table-column prop="deadlineTime" :label="$t('reportHuaningOweFeeDetail.table.endTime')" align="center" />
      <el-table-column prop="oweAmount" :label="$t('reportHuaningOweFeeDetail.table.totalOweAmount')" align="center" />
      <el-table-column prop="curOweAmount"
        :label="`${reportHuaningOweFeeDetailInfo.conditions.year}${$t('common.year')}1-${reportHuaningOweFeeDetailInfo.conditions.month}${$t('common.month')}${$t('reportHuaningOweFeeDetail.table.oweAmount')}`"
        align="center" />
      <el-table-column prop="preOweAmount"
        :label="`${reportHuaningOweFeeDetailInfo.conditions.year}${$t('common.year')}${$t('reportHuaningOweFeeDetail.table.preYearOweAmount')}`"
        align="center" />
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
      layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
  </div>
</template>

<script>
import { queryHuaningOweFeeDetail } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import {getDateYYYYMMDD} from '@/utils/dateUtil'

export default {
  name: 'ReportHuaningOweFeeDetail',
  data() {
    return {
      reportHuaningOweFeeDetailInfo: {
        fees: [],
        conditions: {
          year: new Date().getFullYear(),
          month: new Date().getMonth() + 1
        },
        dateStr: getDateYYYYMMDD()
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
      this.reportHuaningOweFeeDetailInfo.conditions = { ...conditions }
      this.pagination.current = 1
      await this._listReportHuaningOweFeeDetail()
    },
    async _listReportHuaningOweFeeDetail() {
      try {
        const params = {
          ...this.reportHuaningOweFeeDetailInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId()
        }

        const { data, total } = await queryHuaningOweFeeDetail(params)

        this.reportHuaningOweFeeDetailInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        console.error('获取欠费明细数据失败:', error)
      }
    },
    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this._listReportHuaningOweFeeDetail()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owe-fee-detail-container {
  .margin-top {
    margin-top: 20px;
  }
}
</style>