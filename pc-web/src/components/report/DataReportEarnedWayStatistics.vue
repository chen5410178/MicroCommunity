<template>
  <div class="margin-top">
    <div class="text-right">
      <el-button type="primary" size="small" @click="_exportReportEarnedWayExcel">
        <i class="el-icon-download"></i>
        <span>{{ $t('dataReport.export') }}</span>
      </el-button>
    </div>
    <div class="margin-top">
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
        fees: []
      },
      startDate: '',
      endDate: '',
      communityId: ''
    }
  },
  created() {
  },
  methods: {
    open(_param) {
      this.startDate = _param.startDate
      this.endDate = _param.endDate
      this.communityId = _param.communityId
      this._loadDataReportEarnedWayStatisticsData()
    },
    async _loadDataReportEarnedWayStatisticsData() {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.startDate,
          endDate: this.endDate
        }
        const { data } = await queryReceivedWayStatistics(params)
        this.dataReportEarnedWayStatisticsInfo.fees = data
      } catch (error) {
        console.error('Failed to load data:', error)
      }
    },
    async _exportReportEarnedWayExcel() {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.startDate,
          endDate: this.endDate,
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