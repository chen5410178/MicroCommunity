<template>
  <div class="margin-top-sm">
    <el-row :gutter="20">
      <el-col :span="3" class="padding-r-0">
        <el-card class="border-radius">
          <div class="treeview attendance-staff" style="height: 650px;">
            <ul class="list-group text-center border-radius">
              <li v-for="(item, index) in dataReportFeeStatisticsInfo.feeTypeCds" :key="index"
                class="list-group-item node-orgTree"
                :class="{ 'vc-node-selected': dataReportFeeStatisticsInfo.feeTypeCd === item.statusCd }"
                @click="swatchFeeStatisticsFeeTypeCd(item.statusCd)">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="21">
        <el-row>
          <el-col :span="4">
            <el-date-picker v-model="dataReportFeeStatisticsInfo.startDate" type="date"
              :placeholder="$t('dataReport.startDate')" value-format="yyyy-MM-dd" style="width: 100%" />
          </el-col>
          <el-col :span="4">
            <el-date-picker v-model="dataReportFeeStatisticsInfo.endDate" type="date"
              :placeholder="$t('dataReport.endDate')" value-format="yyyy-MM-dd" style="width: 100%" />
          </el-col>
          <el-col :span="4">
            <el-button type="primary" size="small" @click="_qureyDataReportFeeStatistics">
              <i class="el-icon-search"></i>
              {{ $t('dataReport.search') }}
            </el-button>
          </el-col>
          <el-col :span="8" class="text-right">
            <el-button type="primary" size="small" @click="_exportReportFeeExcel">
              <i class="el-icon-download"></i>
              <span>{{ $t('dataReport.export') }}</span>
            </el-button>
          </el-col>
        </el-row>
        <div class="margin-top-sm">
          <el-table :data="dataReportFeeStatisticsInfo.fees" border style="width: 100%">
            <el-table-column prop="floorNum" :label="$t('dataReport.building')" align="center" />
            <el-table-column prop="roomCount" :label="$t('dataReport.roomCount')" align="center" />
            <el-table-column prop="feeRoomCount" :label="$t('dataReport.feeRoomCount')" align="center" />
            <el-table-column prop="hisMonthOweFee" :label="$t('dataReport.historyOwe')" align="center" />
            <el-table-column prop="oweFee" :label="$t('dataReport.totalOwe')" align="center" />
            <el-table-column prop="todayReceivedRoomCount" :label="$t('dataReport.todayPaidCount')" align="center" />
            <el-table-column prop="todayReceivedRoomAmount" :label="$t('dataReport.todayPaidAmount')" align="center" />
            <el-table-column prop="hisOweReceivedRoomCount" :label="$t('dataReport.historyPaidCount')" align="center" />
            <el-table-column prop="hisOweReceivedRoomAmount" :label="$t('dataReport.historyPaidAmount')" align="center" />
            <el-table-column prop="monthReceivedRoomCount" :label="$t('dataReport.monthPaidCount')" align="center" />
            <el-table-column :label="$t('dataReport.remainingCount')" align="center">
              <template slot-scope="scope">
                {{ (scope.row.feeRoomCount - scope.row.monthReceivedRoomCount).toFixed(0) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('dataReport.paidRatio')" align="center">
              <template slot-scope="scope">
                {{ scope.row.feeRoomCount > 0 ?
                  ((scope.row.monthReceivedRoomCount) / (scope.row.feeRoomCount) * 100).toFixed(2) + '%' : '0%' }}
              </template>
            </el-table-column>
            <el-table-column prop="monthReceivedRoomAmount" :label="$t('dataReport.monthPaidAmount')" align="center" />
            <el-table-column prop="curMonthOweFee" :label="$t('dataReport.monthRemaining')" align="center" />
            <el-table-column :label="$t('dataReport.collectionRate')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curReceivableFee > 0 ?
                  ((scope.row.monthReceivedRoomAmount) / (scope.row.curReceivableFee) * 100).toFixed(2) + '%' : '0%' }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { queryDataReportFeeStatistics, exportData, queryFloors } from '@/api/report/dataReportApi'

export default {
  name: 'DataReportFeeStatistics',

  data() {
    return {
      dataReportFeeStatisticsInfo: {
        fees: [],
        feeTypeCds: [],
        floors: [],
        feeTypeCd: '',
        startDate: '',
        endDate: ''
      },
      communityId: ''
    }
  },
  created() {
  },
  methods: {
    open(param) {
      this.communityId = param.communityId
      const now = new Date()
      const startDate = new Date(now.getFullYear(), now.getMonth(), 1)
      const endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0)
      this.dataReportFeeStatisticsInfo.startDate = this._formatDate(startDate)
      this.dataReportFeeStatisticsInfo.endDate = this._formatDate(endDate)
      this._initMethod()
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
        this.dataReportFeeStatisticsInfo.feeTypeCds = data
        this.dataReportFeeStatisticsInfo.feeTypeCd = data[0].statusCd
        this._loadFloors()
      } catch (error) {
        console.error('Failed to load dictionary:', error)
      }
    },
    async _loadFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { apiFloorDataVoList } = await queryFloors(params)
        this.dataReportFeeStatisticsInfo.floors = apiFloorDataVoList
        this.dataReportFeeStatisticsInfo.fees = []

        const _floorIds = []
        for (let i = 0; i < apiFloorDataVoList.length; i++) {
          _floorIds.push(apiFloorDataVoList[i].floorId)
          if (_floorIds.length >= 5) {
            await this._loadDataReportFeeStatisticsData(_floorIds.join(','))
            _floorIds.length = 0
          }
        }

        if (_floorIds.length > 0) {
          await this._loadDataReportFeeStatisticsData(_floorIds.join(','))
        }
      } catch (error) {
        console.error('Failed to load floors:', error)
      }
    },
    async _loadDataReportFeeStatisticsData(_floorIds) {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.dataReportFeeStatisticsInfo.startDate,
          endDate: this.dataReportFeeStatisticsInfo.endDate,
          feeTypeCd: this.dataReportFeeStatisticsInfo.feeTypeCd,
          floorIds: _floorIds
        }
        const { data } = await queryDataReportFeeStatistics(params)
        data.forEach(item => {
          this.dataReportFeeStatisticsInfo.fees.push(item)
        })
      } catch (error) {
        console.error('Failed to load data:', error)
      }
    },
    _qureyDataReportFeeStatistics() {
      this.dataReportFeeStatisticsInfo.fees = []
      this._loadFloors()
    },
    async _exportReportFeeExcel() {
      try {
        const _floorIds = this.dataReportFeeStatisticsInfo.floors.map(item => item.floorId)
        if (!_floorIds || _floorIds.length < 1) {
          this.$message.warning(this.$t('dataReport.noExportData'))
          return
        }

        const params = {
          communityId: this.communityId,
          startDate: this.dataReportFeeStatisticsInfo.startDate,
          endDate: this.dataReportFeeStatisticsInfo.endDate,
          feeTypeCd: this.dataReportFeeStatisticsInfo.feeTypeCd,
          floorIds: _floorIds.join(','),
          pagePath: 'dataReportFeeStatistics'
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
    swatchFeeStatisticsFeeTypeCd(_feeTypeCd) {
      this.dataReportFeeStatisticsInfo.feeTypeCd = _feeTypeCd
      this._loadFloors()
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
}</style>