<template>
  <div class="report-fee-detail-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <el-card class="role-card">
          <ul class="role-list">
            <li 
              v-for="item in menuList" 
              :key="item.key"
              class="role-item"
              :class="{ 'role-item-active': currentTab === item.key }"
              @click="changeTab(item.key)">
              {{ item.label }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="21">
        <report-fee-detail-room v-if="currentTab === 'room'" ref="reportFeeDetailRoom"></report-fee-detail-room>
        <report-fee-detail-owner v-if="currentTab === 'owner'" ref="reportFeeDetailOwner"></report-fee-detail-owner>
        <report-fee-detail-contract v-if="currentTab === 'contract'" ref="reportFeeDetailContract"></report-fee-detail-contract>
        <report-fee-detail-car v-if="currentTab === 'car'" ref="reportFeeDetailCar"></report-fee-detail-car>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ReportFeeDetailRoom from '@/components/report/reportFeeDetailRoom'
import ReportFeeDetailOwner from '@/components/report/reportFeeDetailOwner'
import ReportFeeDetailContract from '@/components/report/reportFeeDetailContract'
import ReportFeeDetailCar from '@/components/report/reportFeeDetailCar'

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
      currentTab: 'room',
      menuList: [
        { key: 'room', label: '房屋费用明细', icon: 'el-icon-house' },
        { key: 'owner', label: '业主费用明细', icon: 'el-icon-user' },
        { key: 'contract', label: '合同费用明细', icon: 'el-icon-document' },
        { key: 'car', label: '车辆费用明细', icon: 'el-icon-truck' }
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
        const refMap = {
          room: 'reportFeeDetailRoom',
          owner: 'reportFeeDetailOwner',
          contract: 'reportFeeDetailContract',
          car: 'reportFeeDetailCar'
        }
        this.$refs[refMap[tab]].initData()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.report-fee-detail-container {
  padding: 20px;
}

.role-card {
  height: 100%;
}

.role-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.role-item {
  padding: 10px;
  text-align: center;
  cursor: pointer;
  border-radius: 4px;
  margin-bottom: 5px;
  transition: all 0.3s;
}

.role-item:hover {
  background-color: #f5f7fa;
}

.role-item-active {
  background-color: #409eff;
  color: white;
}
</style>