<template>
  <div class="report-proficient-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('reportProficient.search.title') }}</span>
        <div class="header-tools">
          <el-button type="primary" size="small" @click="_exportFee">
            <i class="el-icon-upload"></i>
            {{ $t('reportProficient.export') }}
          </el-button>
        </div>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="reportProficientInfo.conditions.feeTypeCd" @change="_changeReporficientFeeTypeCd"
            placeholder="请选择收费类型" style="width:100%">
            <el-option v-for="(item, index) in reportProficientInfo.feeTypeCds" :key="index" :label="item.name"
              :value="item.statusCd">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="reportProficientInfo.conditions.configId" placeholder="请选择收费项" style="width:100%">
            <el-option v-for="(item, index) in reportProficientInfo.feeConfigDtos" :key="index" :label="item.feeName"
              :value="item.configId">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input :placeholder="_getReportProficientRoomName()" v-model="reportProficientInfo.conditions.objName">
          </el-input>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="_queryMethod">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="_resetMethod" style="margin-left: 10px;">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 内容区域 -->
    <el-card class="content-wrapper">
      <el-tabs v-model="reportProficientInfo._currentTab" @tab-click="changeTab(reportProficientInfo._currentTab)">
        <el-tab-pane :label="$t('reportProficient.roomFee')" name="reportProficientRoomFee">
          <report-proficient-room-fee ref="reportProficientRoomFee" v-if="reportProficientInfo._currentTab === 'reportProficientRoomFee'"
            :conditions="reportProficientInfo.conditions">
          </report-proficient-room-fee>
        </el-tab-pane>
        <el-tab-pane :label="$t('reportProficient.carFee')" name="reportProficientCarFee">
          <report-proficient-car-fee ref="reportProficientCarFee" v-if="reportProficientInfo._currentTab === 'reportProficientCarFee'"
            :conditions="reportProficientInfo.conditions">
          </report-proficient-car-fee>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 费用详情弹窗 -->
    <view-fee-detail ref="viewFeeDetail"></view-fee-detail>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getFeeConfigList, exportReportFee } from '@/api/report/reportProficientApi'
import ReportProficientRoomFee from '@/components/report/reportProficientRoomFee'
import ReportProficientCarFee from '@/components/report/reportProficientCarFee'
import ViewFeeDetail from '@/components/report/viewFeeDetail'

export default {
  name: 'ReportProficientList',
  components: {
    ReportProficientRoomFee,
    ReportProficientCarFee,
    ViewFeeDetail
  },
  data() {
    return {
      reportProficientInfo: {
        _currentTab: 'reportProficientRoomFee',
        feeTypeCds: [],
        feeConfigDtos: [],
        conditions: {
          configId: '',
          feeTypeCd: '',
          objName: '',
          objType: ''
        }
      }
    }
  },
  created() {
    this._initData()
    this.changeTab(this.reportProficientInfo._currentTab)
  },
  methods: {
    async _initData() {
      // 获取费用类型字典
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.reportProficientInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取费用类型失败:', error)
      }
    },
    async _changeReporficientFeeTypeCd() {
      try {
        const params = {
          page: 1,
          row: 50,
          feeTypeCd: this.reportProficientInfo.conditions.feeTypeCd,
          valid: '1'
        }
        const { data } = await getFeeConfigList(params)
        this.reportProficientInfo.feeConfigDtos = data
      } catch (error) {
        console.error('获取收费项失败:', error)
      }
    },
    changeTab(tab) {
      this.reportProficientInfo._currentTab = tab
      setTimeout(() => {
        this.$refs[`${this.reportProficientInfo._currentTab}`].open(this.reportProficientInfo.conditions)
      },500)
    },
    _queryMethod() {
      setTimeout(() => {
        this.$refs[`${this.reportProficientInfo._currentTab}`].open(this.reportProficientInfo.conditions)
      },500)
    },
    _resetMethod() {
      this.reportProficientInfo.conditions = {
        configId: '',
        feeTypeCd: '',
        objName: '',
        objType: ''
      }
      this._queryMethod()
    },
    _getReportProficientRoomName() {
      if (this.reportProficientInfo._currentTab === 'reportProficientRoomFee') {
        return this.$t('reportProficient.search.roomPlaceholder')
      }
      return this.$t('reportProficient.search.carPlaceholder')
    },
    async _exportFee() {
      try {
        const objType = this.reportProficientInfo._currentTab === 'reportProficientRoomFee' ? '3333' : '6666'
        const params = {
          ...this.reportProficientInfo.conditions,
          objType,
          pagePath: 'reportProficient'
        }
        const res = await exportReportFee(params)
        this.$message.success(res.msg)
        if (res.code === 0) {
          this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
        }
      } catch (error) {
        console.error('导出失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.report-proficient-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .header-tools {
      float: right;
    }
  }

  .content-wrapper {
    margin-top: 20px;
  }
}
</style>