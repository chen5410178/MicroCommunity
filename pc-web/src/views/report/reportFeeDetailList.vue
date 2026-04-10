<template>
  <div class="report-fee-detail-container">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('reportFeeDetail.search.title') }}</span>
        <div class="header-tools">
          <el-button type="text" @click="toggleMoreCondition" style="margin-right:10px;">
            {{ reportFeeDetailInfo.moreCondition ? $t('reportFeeDetail.hide') : $t('reportFeeDetail.more') }}
          </el-button>
        </div>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-date-picker v-model="reportFeeDetailInfo.conditions.startDate" type="date"
            :placeholder="$t('reportFeeDetail.placeholder.startDate')" style="width:100%">
          </el-date-picker>
        </el-col>
        <el-col :span="6">
          <el-date-picker v-model="reportFeeDetailInfo.conditions.endDate" type="date"
            :placeholder="$t('reportFeeDetail.placeholder.endDate')" style="width:100%">
          </el-date-picker>
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="reportFeeDetailInfo.conditions.objName"
            :placeholder="$t('reportFeeDetail.placeholder.objName')">
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="queryMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="resetMethod">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-show="reportFeeDetailInfo.moreCondition" class="margin-top">
        <el-col :span="6">
          <el-input v-model.trim="reportFeeDetailInfo.conditions.ownerName"
            :placeholder="$t('reportFeeDetail.placeholder.ownerName')">
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="reportFeeDetailInfo.conditions.link"
            :placeholder="$t('reportFeeDetail.placeholder.link')">
          </el-input>
        </el-col>
        <el-col :span="6" v-if="reportFeeDetailInfo.communitys.length > 1">
          <el-select v-model="reportFeeDetailInfo.conditions.communityId" @change="changCommunity" style="width:100%">
            <el-option disabled :value="''" :label="$t('reportFeeDetail.placeholder.community')">
            </el-option>
            <el-option v-for="(item, index) in reportFeeDetailInfo.communitys" :key="index" :label="item.name"
              :value="item.communityId">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
    </el-card>

    <!-- 内容区域 -->
    <el-card class="content-card">
      <el-tabs v-model="reportFeeDetailInfo._currentTab" @tab-click="changeTab">
        <el-tab-pane label="房屋费用明细" name="reportFeeDetailRoom">
          <report-fee-detail-room v-if="reportFeeDetailInfo._currentTab === 'reportFeeDetailRoom'"
            ref="reportFeeDetailRoom">
          </report-fee-detail-room>
        </el-tab-pane>
        <el-tab-pane label="业主费用明细" name="reportFeeDetailOwner">
          <report-fee-detail-owner v-if="reportFeeDetailInfo._currentTab === 'reportFeeDetailOwner'"
            ref="reportFeeDetailOwner">
          </report-fee-detail-owner>
        </el-tab-pane>
        <el-tab-pane label="合同费用明细" name="reportFeeDetailContract">
          <report-fee-detail-contract v-if="reportFeeDetailInfo._currentTab === 'reportFeeDetailContract'"
            ref="reportFeeDetailContract">
          </report-fee-detail-contract>
        </el-tab-pane>
        <el-tab-pane label="车辆费用明细" name="reportFeeDetailCar">
          <report-fee-detail-car v-if="reportFeeDetailInfo._currentTab === 'reportFeeDetailCar'"
            ref="reportFeeDetailCar">
          </report-fee-detail-car>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ReportFeeDetailRoom from '@/components/report/reportFeeDetailRoom'
import ReportFeeDetailOwner from '@/components/report/reportFeeDetailOwner'
import ReportFeeDetailContract from '@/components/report/reportFeeDetailContract'
import ReportFeeDetailCar from '@/components/report/reportFeeDetailCar'
import { loadStaffCommunitys } from '@/api/report/reportFeeDetailApi'

export default {
  name: 'ReportFeeDetailList',
  components: {
    ReportFeeDetailRoom,
    ReportFeeDetailOwner,
    ReportFeeDetailContract,
    ReportFeeDetailCar
  },
  data() {
    return {
      reportFeeDetailInfo: {
        _currentTab: 'reportFeeDetailRoom',
        floors: [],
        moreCondition: false,
        communitys: [],
        conditions: {
          floorId: '',
          objName: '',
          startDate: '',
          endDate: '',
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
    this.initDate()
    this.loadStaffCommunitys()
    this.reportFeeDetailInfo.conditions.communityId = getCommunityId()
    this.changeTab(this.reportFeeDetailInfo._currentTab)
  },
  methods: {
    initDate() {
      const now = new Date()
      const year = now.getFullYear()
      let month = now.getMonth() + 1
      month = month < 10 ? `0${month}` : month

      this.reportFeeDetailInfo.conditions.startDate = `${year}-${month}-01`

      const nextMonth = new Date(year, month, 1)
      const nextYear = nextMonth.getFullYear()
      let nextMonthNum = nextMonth.getMonth() + 1
      nextMonthNum = nextMonthNum < 10 ? `0${nextMonthNum}` : nextMonthNum

      this.reportFeeDetailInfo.conditions.endDate = `${nextYear}-${nextMonthNum}-01`
    },
    async loadStaffCommunitys() {
      try {
        const params = {
          _uid: '123mlkdinkldldijdhuudjdjkkd',
          page: 1,
          row: 100
        }
        const data = await loadStaffCommunitys(params)
        this.reportFeeDetailInfo.communitys = data.communitys
      } catch (error) {
        console.error('加载小区列表失败:', error)
      }
    },
    changeTab(tab) {
      this.reportFeeDetailInfo._currentTab = tab.name || tab
      setTimeout(() => {
        this.$refs[tab.name || tab].open(this.reportFeeDetailInfo.conditions)
      }, 500)

    },
    queryMethod() {
      this.changeTab(this.reportFeeDetailInfo._currentTab)
    },
    resetMethod() {
      this.reportFeeDetailInfo.conditions = {
        floorId: '',
        objName: '',
        startDate: '',
        endDate: '',
        configId: '',
        feeTypeCd: '',
        ownerName: '',
        link: '',
        communityId: getCommunityId()
      }
      this.initDate()
      this.changeTab(this.reportFeeDetailInfo._currentTab)
    },
    changCommunity() {
      this.changeTab(this.reportFeeDetailInfo._currentTab)
    },
    toggleMoreCondition() {
      this.reportFeeDetailInfo.moreCondition = !this.reportFeeDetailInfo.moreCondition
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;

    .header-tools {
      float: right;
    }
  }

  .content-card {
    width: 100%;
  }
}
</style>