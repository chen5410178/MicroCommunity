<template>
  <div class="margin-top-sm">
    <el-row >
      <el-col :span="4">
        <el-date-picker v-model="dataReportOweStatisticsInfo.startDate" type="date"
          :placeholder="$t('dataReport.startDate')" value-format="yyyy-MM-dd" style="width: 100%" />
      </el-col>
      <el-col :span="4">
        <el-date-picker v-model="dataReportOweStatisticsInfo.endDate" type="date"
          :placeholder="$t('dataReport.endDate')" value-format="yyyy-MM-dd" style="width: 100%" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" size="small" @click="_qureyDataReportOweStatistics">
          <i class="el-icon-search"></i>
          {{ $t('dataReport.search') }}
        </el-button>
      </el-col>
      <el-col :span="8" class="text-right">
        <el-button type="primary" size="small" @click="_exportReportOweExcel">
          <i class="el-icon-download"></i>
          <span>{{ $t('dataReport.export') }}</span>
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top-sm">
      <el-table :data="dataReportOweStatisticsInfo.fees" border style="width: 100%">
        <el-table-column prop="floorNum" :label="$t('dataReport.building')" align="center" />
        <el-table-column prop="oweFee" :label="$t('dataReport.oweFee')" align="center" />
        <el-table-column v-for="(item, index) in dataReportOweStatisticsInfo.feeTypeCds" :key="index" :label="item.name"
          align="center">
          <template slot-scope="scope">
            {{ scope.row['oweFee' + item.statusCd] || 0 }}
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="8">
          <span>{{ $t('dataReport.totalOwe') }}：</span>
          <span>{{ dataReportOweStatisticsInfo.feeAmount }}</span>
        </el-col>
        <el-col :span="16" class="text-right">
          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryOweStatistics, exportData } from '@/api/report/dataReportApi'

export default {
  name: 'DataReportOweStatistics',

  data() {
    return {
      dataReportOweStatisticsInfo: {
        fees: [],
        feeTypeCds: [],
        feeAmount: '0',
        startDate: '',
        endDate: ''
      },
      communityId: '',
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._initMethod()
  },
  methods: {
    open(param) {
      this.communityId = param.communityId
      const now = new Date()
      const startDate = new Date(now.getFullYear(), now.getMonth(), 1)
      const endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0)
      this.dataReportOweStatisticsInfo.startDate = this._formatDate(startDate)
      this.dataReportOweStatisticsInfo.endDate = this._formatDate(endDate)
      this._loadDataReportOweStatisticsData(this.page.current, this.page.size)
    },
    _formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return year + '-' + month + '-' + day
    },
    async _initMethod() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.dataReportOweStatisticsInfo.feeTypeCds = data
      } catch (error) {
        console.error('Failed to load dictionary:', error)
      }
    },
    async _loadDataReportOweStatisticsData(_page, _row) {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.dataReportOweStatisticsInfo.startDate,
          endDate: this.dataReportOweStatisticsInfo.endDate,
          page: _page,
          row: _row
        }
        const { data, records } = await queryOweStatistics(params)
        this.dataReportOweStatisticsInfo.fees = data
        this.page.total = records

        let _feeAmount = 0.0
        if (data && data.length > 0) {
          data.forEach(item => {
            _feeAmount += parseFloat(item.oweFee)
          })
        }
        this.dataReportOweStatisticsInfo.feeAmount = _feeAmount.toFixed(2)
      } catch (error) {
        console.error('Failed to load data:', error)
      }
    },
    _qureyDataReportOweStatistics() {
      this.page.current = 1
      this._loadDataReportOweStatisticsData(this.page.current, this.page.size)
    },
    async _exportReportOweExcel() {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.dataReportOweStatisticsInfo.startDate,
          endDate: this.dataReportOweStatisticsInfo.endDate,
          pagePath: 'dataReportOweStatistics'
        }
        const res = await exportData(params)
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('Export failed:', error)
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadDataReportOweStatisticsData(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._loadDataReportOweStatisticsData(val, this.page.size)
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