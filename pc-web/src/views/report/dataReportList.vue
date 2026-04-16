<template>
  <div class="data-report-container">
    <div class="box-card">
      <div class="flex justify-between padding">
        <div class="flex justify-start">
          <div>
            <span>{{ $t('dataReport.timeRange') }}：</span>
            <el-date-picker v-model="dataReportInfo.conditions.startDate" type="date"
              :placeholder="$t('dataReport.startDate')" value-format="yyyy-MM-dd" style="width: 150px" @change="_changeDate" />
            <span>{{ $t('dataReport.to') }}：</span>
            <el-date-picker v-model="dataReportInfo.conditions.endDate" type="date"
              :placeholder="$t('dataReport.endDate')" value-format="yyyy-MM-dd" style="width: 150px" @change="_changeDate" />
          </div>
          <div v-if="dataReportInfo.communitys.length > 1" style="margin-left: 20px">
            <el-select v-model="dataReportInfo.conditions.communityId" :placeholder="$t('dataReport.selectCommunity')"
              style="width: 200px" @change="_changCommunity">
              <el-option v-for="(item, index) in dataReportInfo.communitys" :key="index" :label="item.name"
                :value="item.communityId" />
            </el-select>
          </div>
        </div>
        <div>
          <el-button :type="dataReportInfo.curDay === 'today' ? 'primary' : ''" @click="_changeDate('today')">{{
            $t('dataReport.today') }}</el-button>
          <el-button :type="dataReportInfo.curDay === 'yesterday' ? 'primary' : ''" @click="_changeDate('yesterday')">{{
            $t('dataReport.yesterday') }}</el-button>
          <el-button :type="dataReportInfo.curDay === 'seven' ? 'primary' : ''" @click="_changeDate('seven')">{{
            $t('dataReport.last7Days') }}</el-button>
          <el-button :type="dataReportInfo.curDay === 'thirty' ? 'primary' : ''" @click="_changeDate('thirty')">{{
            $t('dataReport.last30Days') }}</el-button>
        </div>
      </div>

      <div class="flex justify-between ">
        <el-card class="data-report-item">
          <div class="data-report-title">{{ $t('dataReport.feeStatistics') }}</div>
          <div class="flex flex-wrap">
            <div v-for="(item, index) in dataReportInfo.fees" :key="index" class="data-report-card">
              <div class="data-report-card-title">{{ item.name }}</div>
              <div class="data-report-card-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="data-report-item">
          <div class="data-report-title">{{ $t('dataReport.orderStatistics') }}</div>
          <div class="flex flex-wrap">
            <div v-for="(item, index) in dataReportInfo.orders" :key="index" class="data-report-card">
              <div class="data-report-card-title">{{ item.name }}</div>
              <div class="data-report-card-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="data-report-item">
          <div class="data-report-title">{{ $t('dataReport.inoutStatistics') }}</div>
          <div class="flex flex-wrap">
            <div v-for="(item, index) in dataReportInfo.inouts" :key="index" class="data-report-card">
              <div class="data-report-card-title">{{ item.name }}</div>
              <div class="data-report-card-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="data-report-item">
          <div class="data-report-title">{{ $t('dataReport.otherStatistics') }}</div>
          <div class="flex flex-wrap">
            <div v-for="(item, index) in dataReportInfo.others" :key="index" class="data-report-card">
              <div class="data-report-card-title">{{ item.name }}</div>
              <div class="data-report-card-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>
      </div>

      <el-card>
        <el-tabs v-model="dataReportInfo._currentTab" @tab-click="changeTab(dataReportInfo._currentTab)">
          <el-tab-pane label="实收统计" name="dataReportEarnedStatistics" />
          <el-tab-pane label="实收明细" name="dataReportEarnedDetailStatistics" />
          <el-tab-pane label="收款方式统计" name="dataReportEarnedWayStatistics" />
          <el-tab-pane label="欠费统计" name="dataReportOweStatistics" />
          <el-tab-pane label="欠费明细" name="dataReportOweDetailStatistics" />
          <el-tab-pane label="收缴情况" name="dataReportFeeStatistics" />
          <el-tab-pane label="月实收明细" name="dataMonthReceivedStatistics" />
          <el-tab-pane label="月欠费明细" name="dataMonthOweStatistics" />
        </el-tabs>

        <component :is="dataReportInfo._currentTab" :ref="dataReportInfo._currentTab"/>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  queryFeeDataReport,
  queryOrderDataReport,
  queryInoutDataReport,
  queryOthersDataReport,
  listMyEnteredCommunitys
} from '@/api/report/dataReportApi'
import DataReportEarnedStatistics from '@/components/report/DataReportEarnedStatistics'
import DataReportEarnedDetailStatistics from '@/components/report/DataReportEarnedDetailStatistics'
import DataReportEarnedWayStatistics from '@/components/report/DataReportEarnedWayStatistics'
import DataReportOweStatistics from '@/components/report/DataReportOweStatistics'
import DataReportOweDetailStatistics from '@/components/report/DataReportOweDetailStatistics'
import DataReportFeeStatistics from '@/components/report/DataReportFeeStatistics'
import DataMonthReceivedStatistics from '@/components/report/DataMonthReceivedStatistics'
import DataMonthOweStatistics from '@/components/report/DataMonthOweStatistics'

export default {
  name: 'DataReportList',
  components: {
    DataReportEarnedStatistics,
    DataReportEarnedDetailStatistics,
    DataReportEarnedWayStatistics,
    DataReportOweStatistics,
    DataReportOweDetailStatistics,
    DataReportFeeStatistics,
    DataMonthReceivedStatistics,
    DataMonthOweStatistics
  },
  data() {
    return {
      dataReportInfo: {
        curDay: 'thirty',
        _currentTab: 'dataReportEarnedStatistics',
        fees: [],
        orders: [],
        inouts: [],
        others: [],
        communitys: [],
        conditions: {
          startDate: '',
          endDate: '',
          communityId: ''
        }
      }
    }
  },
  created() {
    this._initDate()
    this.dataReportInfo.conditions.communityId = getCommunityId()
    this._loadStaffCommunitys()
    this._loadDataReportFee()
    this._loadDataReportOrder()
    this._loadDataReportInout()
    this._loadDataReportOthers()
  },
  methods: {
    _initDate() {
      const _data = new Date()
      let _month = _data.getMonth() + 1
      let _newDate = ''
      if (_month < 10) {
        _newDate = _data.getFullYear() + '-0' + _month + '-01'
      } else {
        _newDate = _data.getFullYear() + '-' + _month + '-01'
      }
      this.dataReportInfo.conditions.startDate = _newDate
      _data.setMonth(_data.getMonth() + 1)
      _month = _data.getMonth() + 1
      if (_month < 10) {
        _newDate = _data.getFullYear() + '-0' + _month + '-01'
      } else {
        _newDate = _data.getFullYear() + '-' + _month + '-01'
      }
      this.dataReportInfo.conditions.endDate = _newDate
    },
    _changeDate(_day) {
      this.dataReportInfo.curDay = _day
      const _endDate = new Date()
      if (_day === 'today') {
        this.dataReportInfo.conditions.endDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
        this.dataReportInfo.conditions.startDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
      } else if (_day === 'yesterday') {
        _endDate.setDate(_endDate.getDate() - 1)
        this.dataReportInfo.conditions.endDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
        this.dataReportInfo.conditions.startDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
      } else if (_day === 'seven') {
        this.dataReportInfo.conditions.endDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
        _endDate.setDate(_endDate.getDate() - 7)
        this.dataReportInfo.conditions.startDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
      } else if (_day === 'thirty') {
        this.dataReportInfo.conditions.endDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
        _endDate.setDate(_endDate.getDate() - 30)
        this.dataReportInfo.conditions.startDate = _endDate.getFullYear() + '-' + (_endDate.getMonth() + 1) + '-' + _endDate.getDate()
      }
      this._loadDataReportFee()
      this._loadDataReportOrder()
      this._loadDataReportInout()
      this._loadDataReportOthers()
      this.changeTab(this.dataReportInfo._currentTab)
    },
    async _loadDataReportFee() {
      try {
        const { data } = await queryFeeDataReport(this.dataReportInfo.conditions)
        this.dataReportInfo.fees = data
      } catch (error) {
        console.error('Failed to load fee data:', error)
      }
    },
    async _loadDataReportOrder() {
      try {
        const { data } = await queryOrderDataReport(this.dataReportInfo.conditions)
        this.dataReportInfo.orders = data
      } catch (error) {
        console.error('Failed to load order data:', error)
      }
    },
    async _loadDataReportInout() {
      try {
        const { data } = await queryInoutDataReport(this.dataReportInfo.conditions)
        this.dataReportInfo.inouts = data
      } catch (error) {
        console.error('Failed to load inout data:', error)
      }
    },
    async _loadDataReportOthers() {
      try {
        const { data } = await queryOthersDataReport(this.dataReportInfo.conditions)
        this.dataReportInfo.others = data
      } catch (error) {
        console.error('Failed to load other data:', error)
      }
    },
    changeTab(_tab) {
      this.dataReportInfo._currentTab = _tab
      setTimeout(() => {
        this.$refs[_tab].open(this.dataReportInfo.conditions)
      },500)
    },
    async _loadStaffCommunitys() {
      try {
        const { communitys } = await listMyEnteredCommunitys({
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        })
        this.dataReportInfo.communitys = communitys
      } catch (error) {
        console.error('Failed to load communities:', error)
      }
    },
    _changCommunity() {
      this.changeTab(this.dataReportInfo._currentTab)
      this._loadDataReportFee()
      this._loadDataReportOrder()
      this._loadDataReportInout()
      this._loadDataReportOthers()
    }
  }
}
</script>

<style lang="scss" scoped>
.data-report-container {
  padding: 20px;

  .data-report-item {
    width: 24%;
    margin-bottom: 20px;

    .data-report-title {
      font-weight: bold;
      margin-bottom: 15px;
      font-size: 16px;
    }

    .data-report-card {
      width: 24%;
      margin: 5px;
      padding: 10px;
      border: 1px solid #4297E5;
      border-radius: 4px;

      .data-report-card-title {
        font-size: 12px;
        color: #666;
      }

      .data-report-card-value {
        font-size: 18px;
        font-weight: bold;
        margin-top: 5px;
      }
    }
  }

  .margin-bottom {
    margin-bottom: 20px;
  }

  .padding {
    padding: 10px 0;
  }

  .flex {
    display: flex;
  }

  .justify-between {
    justify-content: space-between;
  }

  .justify-start {
    justify-content: flex-start;
  }

  .flex-wrap {
    flex-wrap: wrap;
  }
}
</style>