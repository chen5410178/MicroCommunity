<template>
  <div class="report-huaning-container">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <h5>{{ $t('reportHuaning.search.title') }}</h5>
          </div>
          <div class="">
            <el-row :gutter="20">
              <!-- 费用类型 -->
              <el-col :span="6">
                <el-select v-model="reportHuaningInfo.conditions.feeTypeCd" @change="_changeReporficientFeeTypeCd"
                  style="width:100%" :placeholder="$t('reportHuaning.search.feeType')">
                  <el-option v-for="(item, index) in reportHuaningInfo.feeTypeCds" :key="index" :label="item.name"
                    :value="item.statusCd">
                  </el-option>
                </el-select>
              </el-col>

              <!-- 楼栋 -->
              <el-col :span="6">
                <el-select v-model="reportHuaningInfo.conditions.floorNum" style="width:100%"
                  :placeholder="$t('reportHuaning.search.floor')">
                  <el-option v-for="(item, index) in reportHuaningInfo.floors" :key="index" :label="item.floorNum"
                    :value="item.floorNum">
                  </el-option>
                </el-select>
              </el-col>

              <el-col :span="6">
                <el-button type="primary" @click="_queryMethod">
                  <i class="el-icon-search"></i>
                  <span>{{ $t('common.search') }}</span>
                </el-button>
                <el-button @click="_resetMethod" style="margin-left: 20px;">
                  <i class="el-icon-refresh"></i>
                  <span>{{ $t('common.reset') }}</span>
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="margin-top">
      <div class="white-bg padding-lg">
        <el-tabs v-model="reportHuaningInfo._currentTab" @tab-click="changeTab(reportHuaningInfo._currentTab)">
          <el-tab-pane :label="$t('reportHuaning.tab.oweFee')" name="reportHuaningOweFee">
          </el-tab-pane>
          <el-tab-pane :label="$t('reportHuaning.tab.oweFeeDetail')" name="reportHuaningOweFeeDetail">
          </el-tab-pane>
          <el-tab-pane :label="$t('reportHuaning.tab.payFee')" name="reportHuaningPayFee">
          </el-tab-pane>
        </el-tabs>

        <div v-if="reportHuaningInfo._currentTab === 'reportHuaningOweFee'">
          <report-huaning-owe-fee ref="reportHuaningOweFee"></report-huaning-owe-fee>
        </div>
        <div v-if="reportHuaningInfo._currentTab === 'reportHuaningPayFee'">
          <report-huaning-pay-fee ref="reportHuaningPayFee"></report-huaning-pay-fee>
        </div>
        <div v-if="reportHuaningInfo._currentTab === 'reportHuaningOweFeeDetail'">
          <report-huaning-owe-fee-detail ref="reportHuaningOweFeeDetail"></report-huaning-owe-fee-detail>
        </div>
      </div>
    </el-card>

    <view-fee-detail ref="viewFeeDetail"></view-fee-detail>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { listFloors } from '@/api/report/reportHuaningApi'
import ReportHuaningOweFee from '@/components/report/ReportHuaningOweFee'
import ReportHuaningPayFee from '@/components/report/ReportHuaningPayFee'
import ReportHuaningOweFeeDetail from '@/components/report/ReportHuaningOweFeeDetail'
import ViewFeeDetail from '@/components/report/viewFeeDetail'

export default {
  name: 'ReportHuaningList',
  components: {
    ReportHuaningOweFee,
    ReportHuaningPayFee,
    ReportHuaningOweFeeDetail,
    ViewFeeDetail
  },
  data() {
    return {
      reportHuaningInfo: {
        receivableAmount: '0',
        noEnterRoomCount: '0',
        roomCount: '0',
        freeRoomCount: '0',
        parkingSpaceCount: '0',
        freeParkingSpaceCount: '0',
        shopCount: '0',
        freeShopCount: '0',
        _currentTab: 'reportHuaningOweFee',
        feeTypeCds: [],
        feeConfigDtos: [],
        floors: [],
        moreCondition: false,
        conditions: {
          configId: '',
          feeTypeCd: '',
          floorNum: '',
          year: new Date().getFullYear(),
          month: new Date().getMonth() + 1
        }
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._initData()
    this.changeTab(this.reportHuaningInfo._currentTab)
  },
  methods: {
    async _initData() {
      try {
        // 获取费用类型字典
        const feeTypes = await getDict('pay_fee_config', 'fee_type_cd')
        this.reportHuaningInfo.feeTypeCds = feeTypes

        // 获取楼栋数据
        const params = {
          communityId: this.communityId,
          row: 50,
          page: 1
        }
        const floorData = await listFloors(params)
        this.reportHuaningInfo.floors = floorData.apiFloorDataVoList
      } catch (error) {
        console.error('初始化数据失败:', error)
      }
    },
    changeTab(tab) {
      this.reportHuaningInfo._currentTab = tab
      setTimeout(() => {
        this.$refs[tab].initData(this.reportHuaningInfo.conditions)
      },500)
    },
    _changeReporficientFeeTypeCd() {
      // 费用类型变更处理
    },
    _queryMethod() {
      this.changeTab(this.reportHuaningInfo._currentTab)
    },
    _resetMethod() {
      this.reportHuaningInfo.conditions = {
        configId: '',
        feeTypeCd: '',
        floorNum: '',
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1
      }
      this._queryMethod()
    },
    _listFloorData() {
      // 已迁移到_initData方法中
    },
    _moreCondition() {
      this.reportHuaningInfo.moreCondition = !this.reportHuaningInfo.moreCondition
    }
  }
}
</script>

<style lang="scss" scoped>
.report-huaning-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .white-bg {
    background-color: #fff;
  }

  .padding-lg {
    padding: 20px;
  }

  .ibox-content {
    padding: 15px 20px 20px 20px;
  }
}
</style>