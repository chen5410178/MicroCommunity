<template>
  <div class="report-owe-fee-container">
    <el-table :data="reportHuaningOweFeeInfo.fees" border style="width: 100%" class="margin-top">
      <el-table-column prop="floorNum" :label="$t('reportHuaningOweFee.table.floorNum')" align="center" />
      <el-table-column prop="oweAmount"
        :label="$t('reportHuaningOweFee.table.totalOweAmount', { date: reportHuaningOweFeeInfo.dateStr })"
        align="center" />
      <el-table-column prop="curOweAmount" :label="_getCurYear() + $t('reportHuaningOweFee.table.oweAmount')"
        align="center" />
      <el-table-column prop="preOweAmount" :label="_getPreCurYear()" align="center" />
    </el-table>

    <el-pagination :current-page.sync="pagination.current" :page-size="pagination.size" :total="pagination.total"
      layout="total, prev, pager, next, jumper" @current-change="handlePageChange" class="margin-top" />
  </div>
</template>

<script>
import { queryHuaningOweFee } from '@/api/report/reportHuaningApi'
import { getCommunityId } from '@/api/community/communityApi'
import {getDateYYYYMMDD} from '@/utils/dateUtil'

export default {
  name: 'ReportHuaningOweFee',
  data() {
    return {
      reportHuaningOweFeeInfo: {
        fees: [],
        dateStr:getDateYYYYMMDD(),
        conditions: {}
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
      this.reportHuaningOweFeeInfo.conditions = { ...conditions }
      this.pagination.current = 1
      await this._listReportHuaningOweFee()
    },
    async _listReportHuaningOweFee() {
      try {
        const params = {
          ...this.reportHuaningOweFeeInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId(),
          objType: '3333'
        }

        const { data, total } = await queryHuaningOweFee(params)

        this.reportHuaningOweFeeInfo.fees = data
        this.pagination.total = total
        this.reportHuaningOweFeeInfo.listColumns = []

        if (data.length > 0 && data[0].reportFeeYearCollectionDetailDtos) {
          data[0].reportFeeYearCollectionDetailDtos.forEach(item => {
            this.reportHuaningOweFeeInfo.listColumns.push(`${item.collectionYear}${this.$t('common.year')}`)
          })
        }
      } catch (error) {
        console.error('获取欠费数据失败:', error)
      }
    },
    handlePageChange(currentPage) {
      this.pagination.current = currentPage
      this._listReportHuaningOweFee()
    },
    _getCurYear() {
      const date = new Date()
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      return `${year}${this.$t('common.year')}1-${month}${this.$t('common.month')}`
    },
    _getPreCurYear() {
      const date = new Date()
      const year = date.getFullYear()
      return `${year}${this.$t('reportHuaningOweFee.table.preYearOweAmount')}`
    },
    _showFeeDetail(fee, item) {
      this.$refs.viewFeeDetail.open({
        roomName: fee.objName,
        feeId: fee.feeId,
        configId: fee.configId,
        payerObjId: fee.objId,
        curYear: item.collectionYear
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.report-owe-fee-container {
  .margin-top {
    margin-top: 20px;
  }
}
</style>