<template>
  <div class="report-pay-fee-detail-container">
    <el-row :gutter="20" class="white-bg padding-lg">
      <el-col :span="24">
        <el-tabs v-model="reportPayFeeDetailInfo._currentTab" @tab-click="changeTab(reportPayFeeDetailInfo._currentTab)">
          <el-tab-pane :label="$t('reportPayFeeDetail.payFeeOrder')" name="payFeeOrder">
            <pay-fee-order ref="payFeeOrder" v-if="reportPayFeeDetailInfo._currentTab === 'payFeeOrder'"></pay-fee-order>
          </el-tab-pane>
          <el-tab-pane :label="$t('reportPayFeeDetail.payFeeDetail')" name="payFeeDetail">
            <pay-fee-detail ref="payFeeDetail" v-if="reportPayFeeDetailInfo._currentTab === 'payFeeDetail'"></pay-fee-detail>
          </el-tab-pane>
          <el-tab-pane :label="$t('reportPayFeeDetail.prePayFeeOrder')" name="prePayFeeOrder">
            <pre-pay-fee-order ref="prePayFeeOrder" v-if="reportPayFeeDetailInfo._currentTab === 'prePayFeeOrder'"></pre-pay-fee-order>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PayFeeOrder from '@/components/report/reportPayFeeOrder.vue'
import PayFeeDetail from '@/components/report/payFeeDetail.vue'
import PrePayFeeOrder from '@/components/report/reportPrePayFeeOrder.vue'

export default {
  name: 'ReportPayFeeDetailList',
  components: {
    PayFeeOrder,
    PayFeeDetail,
    PrePayFeeOrder
  },
  data() {
    return {
      reportPayFeeDetailInfo: {
        _currentTab: 'payFeeOrder',
        conditions: {}
      }
    }
  },
  mounted() {
    this.changeTab(this.reportPayFeeDetailInfo._currentTab)
  },
  methods: {
    changeTab(tabName) {
      this.reportPayFeeDetailInfo._currentTab = tabName
      setTimeout(() => {
        this.$refs[tabName].open(this.reportPayFeeDetailInfo.conditions)
      }, 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-pay-fee-detail-container {
  padding: 20px;

  .white-bg {
    background-color: #ffffff;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .padding-lg {
    padding: 20px;
  }
}
</style>