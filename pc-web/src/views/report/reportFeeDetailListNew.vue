<template>
  <div class="report-fee-detail-container">
    <el-row :gutter="20" style="height: 100%;">
      <el-col :span="4" style="height: 100%;">
        <el-card class="left-menu-card" body-style="padding: 0;">
          <div class="menu-list">
            <div 
              v-for="item in menuList" 
              :key="item.key"
              :class="['menu-item', { active: currentTab === item.key }]"
              @click="changeTab(item.key)">
              <i :class="item.icon"></i>
              <span>{{ item.label }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20" style="height: 100%;">
        <report-fee-detail-room v-if="currentTab === 'room'" ref="reportFeeDetailRoom"></report-fee-detail-room>
        <report-fee-detail-owner v-if="currentTab === 'owner'" ref="reportFeeDetailOwner"></report-fee-detail-owner>
        <report-fee-detail-contract v-if="currentTab === 'contract'" ref="reportFeeDetailContract"></report-fee-detail-contract>
        <report-fee-detail-parking-space v-if="currentTab === 'parkingSpace'" ref="reportFeeDetailParkingSpace"></report-fee-detail-parking-space>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ReportFeeDetailRoom from '@/components/report/reportFeeDetailRoom'
import ReportFeeDetailOwner from '@/components/report/reportFeeDetailOwner'
import ReportFeeDetailContract from '@/components/report/reportFeeDetailContract'
import ReportFeeDetailParkingSpace from '@/components/report/reportFeeDetailParkingSpace'

export default {
  name: 'ReportFeeDetailList',
  components: {
    ReportFeeDetailRoom,
    ReportFeeDetailOwner,
    ReportFeeDetailContract,
    ReportFeeDetailParkingSpace
  },
  data() {
    return {
      currentTab: 'room',
      menuList: [
        { key: 'room', label: '房屋费用明细', icon: 'el-icon-house' },
        { key: 'owner', label: '业主费用明细', icon: 'el-icon-user' },
        { key: 'contract', label: '合同费用明细', icon: 'el-icon-document' },
        { key: 'parkingSpace', label: '车位费用明细', icon: 'el-icon-truck' }
      ]
    }
  },
  created() {
    this.$nextTick(() => {
      this.$refs.reportFeeDetailRoom.initData()
    })
  },
  methods: {
    changeTab(tab) {
      this.currentTab = tab
      this.$nextTick(() => {
        this.$refs[`reportFeeDetail${tab.charAt(0).toUpperCase() + tab.slice(1)}`].initData()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-container {
  padding: 20px;
  height: calc(100vh - 140px);

  .left-menu-card {
    height: 100%;

    .menu-list {
      .menu-item {
        display: flex;
        align-items: center;
        padding: 16px 20px;
        cursor: pointer;
        transition: all 0.3s;
        border-left: 3px solid transparent;

        i {
          margin-right: 10px;
          font-size: 18px;
        }

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #ecf5ff;
          color: #409EFF;
          border-left-color: #409EFF;
        }
      }
    }
  }
}
</style>
