<template>
  <div class="margin-top-sm">
    <el-row :gutter="20">
      <el-col :span="3" class="padding-r-0">
        <el-card class="border-radius">
          <div class="treeview attendance-staff" style="height: 650px;">
            <ul class="list-group text-center border-radius">
              <li v-for="(item, index) in dataMonthReceivedStatisticsInfo.feeTypeCds" :key="index"
                class="list-group-item node-orgTree"
                :class="{ 'vc-node-selected': dataMonthReceivedStatisticsInfo.feeTypeCd === item.statusCd }"
                @click="switchMonthReceivedStatisticsFeeTypeCd(item.statusCd)">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="21">
        <el-row>
          <el-col :span="4">
            <el-select v-model="dataMonthReceivedStatisticsInfo.floorId" :placeholder="$t('dataReport.selectBuilding')"
              clearable style="width: 100%">
              <el-option v-for="(item, index) in dataMonthReceivedStatisticsInfo.floors" :key="index"
                :label="item.floorNum" :value="item.floorId" />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-date-picker v-model="dataMonthReceivedStatisticsInfo.feeStartDate" type="date"
              :placeholder="$t('dataReport.feeStartDate')" value-format="yyyy-MM-dd" style="width: 100%" />
          </el-col>
          <el-col :span="4">
            <el-date-picker v-model="dataMonthReceivedStatisticsInfo.feeEndDate" type="date"
              :placeholder="$t('dataReport.feeEndDate')" value-format="yyyy-MM-dd" style="width: 100%" />
          </el-col>
          <el-col :span="4">
            <el-button type="primary" size="small" @click="_qureyDataMonthReceivedStatistics">
              <i class="el-icon-search"></i>
              {{ $t('dataReport.search') }}
            </el-button>
          </el-col>
          <el-col :span="8" class="text-right">
            <el-button type="primary" size="small" @click="_exportReportMonthReceivedFeeExcel">
              <i class="el-icon-download"></i>
              <span>{{ $t('dataReport.export') }}</span>
            </el-button>
          </el-col>
        </el-row>

        <div class="margin-top-sm">
          <el-table :data="dataMonthReceivedStatisticsInfo.fees" border style="width: 100%">
            <el-table-column prop="objName" :label="$t('dataReport.room')" align="center" />
            <el-table-column :label="$t('dataReport.owner')" align="center">
              <template slot-scope="scope">
                {{ scope.row.ownerName }}({{ scope.row.link }})
              </template>
            </el-table-column>
            <el-table-column prop="receivedAmount" :label="$t('dataReport.receivedAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.receivedAmount || 0 }}
              </template>
            </el-table-column>
            <el-table-column prop="feeName" :label="$t('dataReport.feeName')" align="center" />
            <el-table-column prop="curYearMonth" :label="$t('dataReport.receivedPeriod')" align="center" />
            <el-table-column prop="cashierName" :label="$t('dataReport.cashier')" align="center" />
            <el-table-column prop="createTime" :label="$t('dataReport.paymentTime')" align="center" />
          </el-table>

          <el-row class="margin-top">
            <el-col :span="8">
              <span>{{ $t('dataReport.totalReceived') }}：</span>
              <span>{{ dataMonthReceivedStatisticsInfo.feeAmount }}</span>
            </el-col>
            <el-col :span="16" class="text-right">
              <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryMonthReceivedDetail, exportData, queryFloors } from '@/api/report/dataReportApi'

export default {
  name: 'DataMonthReceivedStatistics',

  data() {
    return {
      dataMonthReceivedStatisticsInfo: {
        fees: [],
        feeTypeCds: [],
        floors: [],
        floorId: '',
        feeStartDate: '',
        feeEndDate: '',
        feeTypeCd: '',
        feeAmount: '0'
      },
      startDate: '',
      endDate: '',
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
      this.startDate = param.startDate
      this.endDate = param.endDate
      this.communityId = param.communityId
      this._loadMonthReceivedFloors()
      this._loadDataMonthReceivedStatisticsData()
    },
    async _initMethod() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.dataMonthReceivedStatisticsInfo.feeTypeCds = data
        this.dataMonthReceivedStatisticsInfo.feeTypeCd = data[0].statusCd
        const now = new Date()
        const startDate = new Date(now.getFullYear(), now.getMonth(), 1)
        const endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0)
        this.dataMonthReceivedStatisticsInfo.feeStartDate = this._formatDate(startDate)
        this.dataMonthReceivedStatisticsInfo.feeEndDate = this._formatDate(endDate)
      } catch (error) {
        console.error('Failed to load dictionary:', error)
      }
    },
    _formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return year + '-' + month + '-' + day
    },
    async _loadMonthReceivedFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { apiFloorDataVoList } = await queryFloors(params)
        this.dataMonthReceivedStatisticsInfo.floors = apiFloorDataVoList
      } catch (error) {
        console.error('Failed to load floors:', error)
      }
    },
    async _loadDataMonthReceivedStatisticsData() {
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          feeStartDate: this.dataMonthReceivedStatisticsInfo.feeStartDate,
          feeEndDate: this.dataMonthReceivedStatisticsInfo.feeEndDate,
          feeTypeCd: this.dataMonthReceivedStatisticsInfo.feeTypeCd,
          floorId: this.dataMonthReceivedStatisticsInfo.floorId
        }
        const { data, records, sumTotal } = await queryMonthReceivedDetail(params)
        this.dataMonthReceivedStatisticsInfo.fees = data
        this.page.total = records
        this.dataMonthReceivedStatisticsInfo.feeAmount = sumTotal
      } catch (error) {
        console.error('Failed to load data:', error)
      }
    },
    async _exportReportMonthReceivedFeeExcel() {
      try {
        const params = {
          communityId: this.communityId,
          feeStartDate: this.dataMonthReceivedStatisticsInfo.feeStartDate,
          feeEndDate: this.dataMonthReceivedStatisticsInfo.feeEndDate,
          feeTypeCd: this.dataMonthReceivedStatisticsInfo.feeTypeCd,
          floorId: this.dataMonthReceivedStatisticsInfo.floorId,
          pagePath: 'dataMonthReceivedStatistics'
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
    _qureyDataMonthReceivedStatistics() {
      this.page.current = 1
      this._loadDataMonthReceivedStatisticsData()
    },
    switchMonthReceivedStatisticsFeeTypeCd(_feeTypeCd) {
      this.dataMonthReceivedStatisticsInfo.feeTypeCd = _feeTypeCd
      this._loadDataMonthReceivedStatisticsData()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadDataMonthReceivedStatisticsData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._loadDataMonthReceivedStatisticsData()
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

.padding-r-0 {
  padding-right: 0;
}

.border-radius {
  border-radius: 4px;
}

.list-group {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list-group-item {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.list-group-item:hover {
  background-color: #f5f5f5;
}

.vc-node-selected {
  background-color: #409EFF;
  color: white;
}

.treeview {
  overflow-y: auto;
}
</style>