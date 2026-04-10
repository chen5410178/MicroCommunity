<template>
  <div class="margin-top">
    <el-row class="margin-top-lg">
      <el-col :span="4">
        <el-input v-model="dataReportOweDetailStatisticsInfo.objName" :placeholder="$t('dataReport.roomNumber')"
          clearable />
      </el-col>
      <el-col :span="4">
        <el-input v-model="dataReportOweDetailStatisticsInfo.ownerName" :placeholder="$t('dataReport.ownerName')"
          clearable />
      </el-col>
      <el-col :span="4">
        <el-input v-model="dataReportOweDetailStatisticsInfo.link" :placeholder="$t('dataReport.phoneNumber')"
          clearable />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" size="small" @click="_qureyDataReportOweDetailStatistics">
          <i class="el-icon-search"></i>
          {{ $t('dataReport.search') }}
        </el-button>
        <el-button type="default" size="small" @click="_resetDataReportOweDetailStatistics">
          <i class="el-icon-refresh"></i>
          {{ $t('dataReport.reset') }}
        </el-button>
      </el-col>
      <el-col :span="8" class="text-right">
        <el-button type="primary" size="small" @click="_exportReportOweDetailExcel">
          <i class="el-icon-download"></i>
          <span>{{ $t('dataReport.export') }}</span>
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="dataReportOweDetailStatisticsInfo.fees" border style="width: 100%">
        <el-table-column prop="roomName" :label="$t('dataReport.room')" align="center" />
        <el-table-column prop="ownerName" :label="$t('dataReport.owner')" align="center" />
        <el-table-column prop="oweFee" :label="$t('dataReport.oweFee')" align="center" />
        <el-table-column v-for="(item, index) in dataReportOweDetailStatisticsInfo.feeTypeCds" :key="index"
          :label="item.name" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row['oweFee' + item.statusCd]">
              <div v-for="(feeItem, fIndex) in scope.row['oweFee' + item.statusCd]" :key="fIndex">
                {{ feeItem.feeName }}({{ feeItem.endTime }}~{{ feeItem.deadlineTime }})={{ feeItem.amountOwed }}
              </div>
            </div>
            <div v-else>
              0
            </div>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="24" class="text-right">
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
import { queryOweDetailStatistics, exportData } from '@/api/report/dataReportApi'

export default {
  name: 'DataReportOweDetailStatistics',

  data() {
    return {
      dataReportOweDetailStatisticsInfo: {
        fees: [],
        feeTypeCds: [],
        objName: '',
        ownerName: '',
        link: '',
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
    open(param){
      this.startDate = param.startDate
      this.endDate = param.endDate
      this.communityId = param.communityId
      this._loadDataReportOweDetailStatisticsData(this.page.current, this.page.size)
    },
    async _initMethod() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd_show')
        this.dataReportOweDetailStatisticsInfo.feeTypeCds = data
      } catch (error) {
        console.error('Failed to load dictionary:', error)
      }
    },
    async _loadDataReportOweDetailStatisticsData(_page, _row) {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.startDate,
          endDate: this.endDate,
          objName: this.dataReportOweDetailStatisticsInfo.objName,
          ownerName: this.dataReportOweDetailStatisticsInfo.ownerName,
          link: this.dataReportOweDetailStatisticsInfo.link,
          page: _page,
          row: _row
        }
        const { data, records } = await queryOweDetailStatistics(params)
        this.dataReportOweDetailStatisticsInfo.fees = data
        this.page.total = records

        let _feeAmount = 0.0
        if (data && data.length > 0) {
          data.forEach(item => {
            _feeAmount += parseFloat(item.oweFee)
          })
        }
        this.dataReportOweDetailStatisticsInfo.feeAmount = _feeAmount.toFixed(2)
      } catch (error) {
        console.error('Failed to load data:', error)
      }
    },
    async _exportReportOweDetailExcel() {
      try {
        const params = {
          communityId: this.communityId,
          startDate: this.startDate,
          endDate: this.endDate,
          objName: this.dataReportOweDetailStatisticsInfo.objName,
          ownerName: this.dataReportOweDetailStatisticsInfo.ownerName,
          link: this.dataReportOweDetailStatisticsInfo.link,
          pagePath: 'dataReportOweDetailStatistics'
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
    _qureyDataReportOweDetailStatistics() {
      this.page.current = 1
      this._loadDataReportOweDetailStatisticsData(this.page.current, this.page.size)
    },
    _resetDataReportOweDetailStatistics() {
      this.dataReportOweDetailStatisticsInfo.objName = ''
      this.dataReportOweDetailStatisticsInfo.ownerName = ''
      this.dataReportOweDetailStatisticsInfo.link = ''
      this._qureyDataReportOweDetailStatistics()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._loadDataReportOweDetailStatisticsData(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._loadDataReportOweDetailStatisticsData(val, this.page.size)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}

.text-right {
  text-align: right;
}
</style>