﻿<template>
  <div class="report-fee-summary-container animated fadeInRight">
    <el-row class="flex justify-start" :gutter="20">
      <el-col :span="3" class="padding-r-0">
        <select-community-floor ref="selectCommunityFloor" />
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportFeeSummary.queryCondition') }}</span>
          </div>
          <div class="text item">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-date-picker v-model="reportFeeSummaryInfo.conditions.startMonth" type="month"
                  :placeholder="$t('reportFeeSummary.selectStartDate')" value-format="yyyy-MM" style="width: 100%"
                  @change="handleMonthChange" />
              </el-col>
              <el-col :span="4">
                <el-date-picker v-model="reportFeeSummaryInfo.conditions.endMonth" type="month"
                  :placeholder="$t('reportFeeSummary.selectEndDate')" value-format="yyyy-MM" style="width: 100%" @change="handleMonthChange" />
              </el-col>
         <el-col :span="4">
                <el-select v-model="reportFeeSummaryInfo.conditions.feeTypeCd"
                  :placeholder="$t('reportFeeSummary.selectFeeType')" style="width: 100%" clearable>
                  <el-option v-for="(item, index) in reportFeeSummaryInfo.feeTypeCds" :key="index" :label="item.name"
                    :value="item.statusCd" />
                </el-select>
              </el-col>
              <el-col v-if="reportFeeSummaryInfo.communitys.length > 1" :span="4">
                <el-select v-model="reportFeeSummaryInfo.conditions.communityId"
                  :placeholder="$t('reportFeeSummary.selectCommunity')" style="width: 100%" @change="_changCommunity">
                  <el-option v-for="(item, index) in reportFeeSummaryInfo.communitys" :key="index" :label="item.name"
                    :value="item.communityId" />
                </el-select>
              </el-col>
     
              <el-col :span="4">
                <el-button type="primary" @click="_queryMethod">
                  <i class="el-icon-search"></i>
                  {{ $t('common.search') }}
                </el-button>
                <el-button @click="_resetMethod">
                  <i class="el-icon-refresh"></i>
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>



            <el-row v-show="reportFeeSummaryInfo.moreCondition" class="margin-top">
              <el-col :span="24">
                <el-checkbox-group v-model="reportFeeSummaryInfo.configIds">
                  <el-checkbox v-for="(item, index) in reportFeeSummaryInfo.feeConfigs" :key="index"
                    :label="item.configId">
                    {{ item.feeName }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ $t('reportFeeSummary.feeSummaryTable') }}</span>
              <span class="el-icon-info popover-show" style="cursor: pointer;"></span>
              <span>({{ $t('reportFeeSummary.updateDaily') }})</span>
            </div>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="_exportExcel">
                <i class="el-icon-download"></i>
                {{ $t('common.export') }}
              </el-button>
            </div>
          </div>
          <div class="text item">
            <el-table :data="reportFeeSummaryInfo.fees" border style="width: 100%">
              <el-table-column prop="roomCount" :label="$t('reportFeeSummary.totalRooms')" align="center" />
              <el-table-column prop="feeRoomCount" :label="$t('reportFeeSummary.chargeRooms')" align="center" />
              <el-table-column prop="oweRoomCount" :label="$t('reportFeeSummary.arrearsRooms')" align="center" />
              <el-table-column :label="$t('reportFeeSummary.arrearsFormula')" align="center" colspan="3">
                <el-table-column prop="hisOweFee" :label="$t('reportFeeSummary.historyArrears')" align="center" />
                <el-table-column prop="curOweFee" :label="$t('reportFeeSummary.currentArrears')" align="center" />
                <el-table-column :label="$t('reportFeeSummary.totalArrears')" align="center">
                  <template slot-scope="scope">
                    {{ (scope.row.curOweFee + scope.row.hisOweFee).toFixed(2) }}
                  </template>
                </el-table-column>
              </el-table-column>
              <el-table-column :label="$t('reportFeeSummary.actualPaymentFormula')" align="center" colspan="4">
                <el-table-column prop="hisReceivedFee" :label="$t('reportFeeSummary.arrearsRecovery')" align="center" />
                <el-table-column :label="$t('reportFeeSummary.currentPartial')" align="center">
                  <template slot-scope="scope">
                    {{ (scope.row.receivedFee - scope.row.hisReceivedFee - scope.row.preReceivedFee).toFixed(2) }}
                  </template>
                </el-table-column>
                <el-table-column prop="preReceivedFee" :label="$t('reportFeeSummary.advancePayment')" align="center" />
                <el-table-column prop="receivedFee" :label="$t('reportFeeSummary.actualPayment')" align="center" />
              </el-table-column>
              <el-table-column prop="curReceivableFee" :label="$t('reportFeeSummary.currentReceivable')"
                align="center" />
              <el-table-column :label="$t('reportFeeSummary.currentActual')" align="center">
                <template slot-scope="scope">
                  {{ (scope.row.curReceivableFee - scope.row.curOweFee).toFixed(2) }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('reportFeeSummary.roomChargeRate')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.feeRoomCount > 0 ? (((scope.row.feeRoomCount - scope.row.oweRoomCount) /
                    scope.row.feeRoomCount * 100).toFixed(2) + '%') : '0%' }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('reportFeeSummary.chargeRate')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.curReceivableFee > 0 ? (((scope.row.curReceivableFee - scope.row.curOweFee) /
                    scope.row.curReceivableFee * 100).toFixed(2) + '%') : '0%' }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('reportFeeSummary.clearanceRate')" align="center">
                <template slot-scope="scope">
                  {{ (scope.row.hisReceivedFee + scope.row.hisOweFee) > 0 ? ((scope.row.hisReceivedFee /
                    (scope.row.hisReceivedFee + scope.row.hisOweFee) * 100).toFixed(2) + '%') : '0%' }}
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>

        <div class="margin-top">
          <floor-fee-summary ref="floorFeeSummary" />
        </div>
        <div class="margin-top">
          <config-fee-summary ref="configFeeSummary" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDict, getCommunityId } from '@/api/community/communityApi'
import { queryReportFeeSummary, exportData, queryCommunitys, queryFeeConfigs } from '@/api/report/reportFeeSummaryApi'
import SelectCommunityFloor from '@/components/report/selectCommunityFloor'
import FloorFeeSummary from '@/components/report/floorFeeSummary'
import ConfigFeeSummary from '@/components/report/configFeeSummary'

export default {
  name: 'ReportFeeSummaryList',
  components: {
    SelectCommunityFloor,
    FloorFeeSummary,
    ConfigFeeSummary
  },
  data() {
    return {
      reportFeeSummaryInfo: {
        fees: [],
        feeConfigs: [],
        floors: [],
        configIds: [],
        feeTypeCds: [],
        communitys: [],
        moreCondition: false,
        conditions: {
          floorId: '',
          objName: '',
          startDate: '',
          endDate: '',
          startMonth: '',
          endMonth: '',
          configId: '',
          feeTypeCd: '',
          ownerName: '',
          link: '',
          communityId: ''
        }
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.reportFeeSummaryInfo.conditions.communityId = await getCommunityId()
      this._initDate()
      this._loadStaffCommunitys()
      this._listFeeConfigs()
      this.getFeeTypeDict()
      this.$refs.selectCommunityFloor.open({
        callBack: (floor) => {
          this.reportFeeSummaryInfo.conditions.floorId = floor.floorId
          this._listFees()
        }
      })
      this._listFees()
    },
    _initDate() {
      const now = new Date()
      const year = now.getFullYear()
      const month = now.getMonth() + 1
      const monthStr = month.toString().padStart(2, '0')

      this.reportFeeSummaryInfo.conditions.startMonth = `${year}-${monthStr}`
      this.reportFeeSummaryInfo.conditions.endMonth = `${year}-${monthStr}`

      this._convertMonthToDate()
    },
    handleMonthChange() {
      if (this.reportFeeSummaryInfo.conditions.startMonth && this.reportFeeSummaryInfo.conditions.endMonth) {
        const start = new Date(this.reportFeeSummaryInfo.conditions.startMonth)
        const end = new Date(this.reportFeeSummaryInfo.conditions.endMonth)

        if (start > end) {
          this.$message.error(this.$t('reportFeeSummary.dateError'))
          this.reportFeeSummaryInfo.conditions.endMonth = ''
        }
      }
      this._convertMonthToDate()
    },
    _convertMonthToDate() {
      if (this.reportFeeSummaryInfo.conditions.startMonth) {
        this.reportFeeSummaryInfo.conditions.startDate = `${this.reportFeeSummaryInfo.conditions.startMonth}-01`
      }
      if (this.reportFeeSummaryInfo.conditions.endMonth) {
        const [year, month] = this.reportFeeSummaryInfo.conditions.endMonth.split('-').map(Number)
        const lastDay = new Date(year, month, 0).getDate()
        this.reportFeeSummaryInfo.conditions.endDate = `${year}-${month.toString().padStart(2, '0')}-${lastDay}`
      }
    },
    handleDateChange() {},
    async getFeeTypeDict() {
      try {
        this.reportFeeSummaryInfo.feeTypeCds = await getDict('pay_fee_config', 'fee_type_cd')
      } catch (error) {
        console.error('Failed to get fee type dict:', error)
      }
    },
    async _listFees() {
      try {
        const params = {
          page: 1,
          row: 100,
          ...this.reportFeeSummaryInfo.conditions,
        }
        if (this.reportFeeSummaryInfo.configIds.length > 0) {
          params.configIds = this.reportFeeSummaryInfo.configIds.join(',');
        }

        // 定义需要查询的字段类型
        const queryTypes = [
          'roomCount',
          'feeRoomCount',
          'oweRoomCount',
          'hisOweFee',
          'curOweFee',
          'curReceivableFee',
          'hisReceivedFee',
          'preReceivedFee',
          'receivedFee'
        ]

        // 合并结果的对象
        const mergedResult = {}

        // 串行查询每个字段
        for (const queryType of queryTypes) {
          try {
            const queryParams = { ...params, queryType }
            const { data } = await queryReportFeeSummary(queryParams)
            if (data && data.length > 0 && data[0]) {
              // 将返回的数据合并到结果对象中
              Object.assign(mergedResult, data[0])
            }
          } catch (error) {
            console.error(`Failed to query ${queryType}:`, error)
            // 如果某个字段查询失败，设置为默认值
            const fieldName = queryType === 'roomCount' || queryType === 'feeRoomCount' || queryType === 'oweRoomCount' ? queryType : queryType
            mergedResult[fieldName] = queryType.includes('Count') ? 0 : 0.00
          }
        }

        // 将合并后的结果放入数组中（因为表格期望数组格式）
        this.reportFeeSummaryInfo.fees = [mergedResult]

        // 通知子组件查询（子组件内部自己查询）
        if (this.$refs.floorFeeSummary) {
          this.$refs.floorFeeSummary.notify(params)
        }
        if (this.$refs.configFeeSummary) {
          this.$refs.configFeeSummary.notify(params)
        }
      } catch (error) {
        console.error('Failed to query fee summary:', error)
        this.$message.error(this.$t('reportFeeSummary.queryFailed') || '查询失败，请稍后重试')
      }
    },
    async _loadStaffCommunitys() {
      try {
        const data = await queryCommunitys()
        this.reportFeeSummaryInfo.communitys = data.communitys
      } catch (error) {
        console.error('Failed to load communities:', error)
      }
    },
    async _listFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.reportFeeSummaryInfo.conditions.communityId,
          isDefault: 'F'
        }

        const data = await queryFeeConfigs(params)
        this.reportFeeSummaryInfo.feeConfigs = data.feeConfigs
      } catch (error) {
        console.error('Failed to list fee configs:', error)
      }
    },
    _queryMethod() {
      this._listFees()
    },
    _resetMethod() {
      this.reportFeeSummaryInfo.conditions = {
        ...this.reportFeeSummaryInfo.conditions,
        roomNum: '',
        ownerName: '',
        link: '',
        feeTypeCd: '',
        floorName: '',
        floorId: '',
        unitId: '',
        configId: ''
      }
      this.reportFeeSummaryInfo.configIds = []
      this._listFees()
    },
    async _exportExcel() {
      try {
        const params = {
          ...this.reportFeeSummaryInfo.conditions,
          pagePath: 'reportFeeSummary'
        }

        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        console.error('Failed to export:', error)
        this.$message.error(this.$t('reportFeeSummary.exportFailed'))
      }
    },
    _changCommunity() {
      this._listFeeConfigs()
      this._listFees()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-summary-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .padding-r-0 {
    padding-right: 0;
  }

  .popover-show {
    margin-left: 10px;
    color: #409EFF;
  }

  .el-checkbox {
    margin-right: 15px;
  }
}
</style>