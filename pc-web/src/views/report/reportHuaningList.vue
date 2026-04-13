<template>
  <div class="report-huaning-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <el-card class="menu-card">
          <ul class="menu-list">
            <li
              v-for="item in menuList"
              :key="item.key"
              class="menu-item"
              :class="{ 'menu-item-active': currentTab === item.key }"
              @click="changeTab(item.key)">
              {{ item.label }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="21">
        <report-huaning-owe-fee v-if="currentTab === 'oweFee'" ref="reportHuaningOweFee"></report-huaning-owe-fee>
        <report-huaning-owe-fee-detail v-if="currentTab === 'oweFeeDetail'" ref="reportHuaningOweFeeDetail"></report-huaning-owe-fee-detail>
        <report-huaning-pay-fee v-if="currentTab === 'payFee'" ref="reportHuaningPayFee"></report-huaning-pay-fee>
      </el-col>
    </el-row>

    <view-fee-detail ref="viewFeeDetail"></view-fee-detail>
  </div>
</template>

<script>
import ReportHuaningOweFee from '@/components/report/ReportHuaningOweFee'
import ReportHuaningOweFeeDetail from '@/components/report/ReportHuaningOweFeeDetail'
import ReportHuaningPayFee from '@/components/report/ReportHuaningPayFee'
import ViewFeeDetail from '@/components/report/viewFeeDetail'

export default {
  name: 'ReportHuaningList',
  components: {
    ReportHuaningOweFee,
    ReportHuaningOweFeeDetail,
    ReportHuaningPayFee,
    ViewFeeDetail
  },
  data() {
    return {
      currentTab: 'oweFee',
      menuList: [
        { key: 'oweFee', label: this.$t('reportHuaning.tab.oweFee') },
        { key: 'oweFeeDetail', label: this.$t('reportHuaning.tab.oweFeeDetail') },
        { key: 'payFee', label: this.$t('reportHuaning.tab.payFee') }
      ]
    }
  },
  created() {
    this.$nextTick(() => {
      this.$refs.reportHuaningOweFee.initData()
    })
  },
  methods: {
    changeTab(tab) {
      this.currentTab = tab
      this.$nextTick(() => {
        const refMap = {
          oweFee: 'reportHuaningOweFee',
          oweFeeDetail: 'reportHuaningOweFeeDetail',
          payFee: 'reportHuaningPayFee'
        }
        if (this.$refs[refMap[tab]]) {
          this.$refs[refMap[tab]].initData()
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.report-huaning-container {
  padding: 20px;
}

.menu-card {
  height: 100%;
}

.menu-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  padding: 10px;
  text-align: center;
  cursor: pointer;
  border-radius: 4px;
  margin-bottom: 5px;
  transition: all 0.3s;
}

.menu-item:hover {
  background-color: #f5f7fa;
}

.menu-item-active {
  background-color: #409eff;
  color: white;
}
</style>