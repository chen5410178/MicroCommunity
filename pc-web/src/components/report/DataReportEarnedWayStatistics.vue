<template>
  <div class="margin-top-sm">
    <el-row >
      <el-col :span="4">
        <el-date-picker v-model="dataReportEarnedWayStatisticsInfo.startDate" type="date"
          :placeholder="$t('dataReport.startDate')" value-format="yyyy-MM-dd" style="width: 100%" />
      </el-col>
      <el-col :span="4">
        <el-date-picker v-model="dataReportEarnedWayStatisticsInfo.endDate" type="date"
          :placeholder="$t('dataReport.endDate')" value-format="yyyy-MM-dd" style="width: 100%" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" size="small" @click="_qureyDataReportEarnedWayStatistics">
          <i class="el-icon-search"></i>
          {{ $t('dataReport.search') }}
        </el-button>
      </el-col>
      <el-col :span="8" class="text-right">
        <el-button type="primary" size="small" @click="_exportReportEarnedWayExcel">
          <i class="el-icon-download"></i>
          <span>{{ $t('dataReport.export') }}</span>
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top-sm">
      <el-table :data="[dataReportEarnedWayStatisticsInfo.fees]" border style="width: 100%">
        <el-table-column v-for="(item, index) in dataReportEarnedWayStatisticsInfo.fees" :key="index" :label="item.name"
          align="center">
          <template>
            {{ item.receivedAmount || 0 }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { queryReceivedWayStatistics, exportData } from '@/api/report/dataReportApi'

export default {
  name: 'DataReportEarnedWayStatistics',

  data() {
    return {
      dataReportEarnedWayStatisticsInfo: {
        fees: [],
        startDate: '',
        endDate: ''
      },
      communityId: ''
    }
  },
  created() {
  },
  methods: {
    open(_param) {
      this.communityId = _param.communityId
      const now = new Date()
      const startDate = new Date(now.getFullYear(), now.getMonth(), 1)
      const endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0)
      this.dataReportEarnedWayStatisticsInfo.startDate = this._formatDate(startDate)
      this.dataReportEarnedWayStatisticsInfo.endDate = this._formatDate(endDate)
      this._loadDataReportEarnedWayStatisticsData()
    },
    _formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return year + '-' + month + '-' + day
    },
    async _loadDataReportEarnedWayStatisticsData() {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.dataReportEarnedWayStatisticsInfo.startDate,
          endDate: this.dataReportEarnedWayStatisticsInfo.endDate
        }
        const { data } = await queryReceivedWayStatistics(params)
        this.dataReportEarnedWayStatisticsInfo.fees = data
      } catch (error) {
        console.error('Failed to load data:', error)
      }
    },
    _qureyDataReportEarnedWayStatistics() {
      this._loadDataReportEarnedWayStatisticsData()
    },
    async _exportReportEarnedWayExcel() {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.dataReportEarnedWayStatisticsInfo.startDate,
          endDate: this.dataReportEarnedWayStatisticsInfo.endDate,
          pagePath: 'dataReportEarnedWayStatistics'
        }
        const res = await exportData(params)
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('Export failed:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.text-right {
  text-align: right;
}
</style>