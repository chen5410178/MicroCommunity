<template>
  <div class="animated fadeInRight padding">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="box-card">
          <div class="treeview">
            <ul class="list-group">
              <li v-for="(item, index) in parkingAreas" :key="index" class="list-group-item"
                :class="{ 'vc-node-selected': conditions.paNum === item.num }" @click="handleSwitchParkingArea(item)">
                {{ item.num }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="21" class="right-panel">
        <el-card class="box-card" >
          <div slot="header" class="flex justify-between">
            <span>{{ $t('tempCarPayment.searchTitle') }}</span>
          </div>
          <el-form :inline="true" :model="conditions" class="demo-form-inline">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-form-item>
                  <el-input v-model="conditions.carNum" :placeholder="$t('tempCarPayment.carNumPlaceholder')" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-select v-model="conditions.state" :placeholder="$t('tempCarPayment.carStatusPlaceholder')"
                    style="width:100%">
                    <el-option label="" :value="null">{{ $t('tempCarPayment.selectCarStatus') }}</el-option>
                    <el-option value="100300" :label="$t('tempCarPayment.inStatus')" />
                    <el-option value="100400" :label="$t('tempCarPayment.paidStatus')" />
                    <el-option value="100500" :label="$t('tempCarPayment.outStatus')" />
                    <el-option value="100600" :label="$t('tempCarPayment.repayStatus')" />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-date-picker v-model="conditions.startTime" type="datetime"
                    :placeholder="$t('tempCarPayment.entryStartTime')" style="width:100%" />
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item>
                  <el-date-picker v-model="conditions.endTime" type="datetime"
                    :placeholder="$t('tempCarPayment.entryEndTime')" style="width:100%" />
                </el-form-item>
              </el-col>

              <el-col :span="4" >
                <el-button type="primary" @click="queryCarInoutMethod">{{ $t('tempCarPayment.search') }}</el-button>
                <el-button @click="resetCarInoutMethod">{{ $t('tempCarPayment.reset') }}</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>

        <el-card class="box-card" style="margin-top:20px">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('tempCarPayment.inParkingTitle') }}</span>
          </div>
          <el-table :data="payments" border style="width:100%">
            <el-table-column prop="inoutId" :label="$t('tempCarPayment.inoutId')" align="center" />
            <el-table-column :label="$t('tempCarPayment.carStatus')" align="center">
              <template slot-scope="scope">
                {{ getCarStatusText(scope.row.carInout) }}
              </template>
            </el-table-column>
            <el-table-column prop="carNum" :label="$t('tempCarPayment.carNum')" align="center" />
            <el-table-column prop="inTime" :label="$t('tempCarPayment.inTime')" align="center" />
            <el-table-column prop="payTypeName" :label="$t('tempCarPayment.payType')" align="center" />
            <el-table-column prop="payCharge" :label="$t('tempCarPayment.payableAmount')" align="center" />
            <el-table-column prop="realCharge" :label="$t('tempCarPayment.paidAmount')" align="center" />
            <el-table-column prop="createTime" :label="$t('tempCarPayment.payTime')" align="center" />
          </el-table>

          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" style="margin-top:20px" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listParkingAreas, getCarInoutPaymentList } from '@/api/car/tempCarPaymentApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'TempCarPaymentList',
  data() {
    return {
      parkingAreas: [],
      payments: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      conditions: {
        paNum: '',
        state: null,
        carNum: '',
        startTime: null,
        endTime: null,
        iotApiCode: 'listCarInoutPaymentBmoImpl'
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadParkingAreas()
  },
  methods: {
    async loadParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const res = await listParkingAreas(params)
        this.parkingAreas = res.parkingAreas
        if (this.parkingAreas.length > 0) {
          this.handleSwitchParkingArea(this.parkingAreas[0])
        }
      } catch (error) {
        console.error('加载停车区域失败:', error)
        this.$message.error(this.$t('tempCarPayment.loadParkingAreaError'))
      }
    },

    handleSwitchParkingArea(parkingArea) {
      this.conditions.paNum = parkingArea.num
      this.queryCarInoutMethod()
    },

    async queryCarInoutMethod() {
      try {
        const params = {
          ...this.conditions,
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId
        }

        // 清理参数
        if (params.carNum) params.carNum = params.carNum.trim()
        if (params.startTime) params.startTime = this.formatDate(params.startTime)
        if (params.endTime) params.endTime = this.formatDate(params.endTime)

        const res = await getCarInoutPaymentList(params)
        this.payments = res.data
        this.pagination.total = res.total
      } catch (error) {
        console.error('查询失败:', error)
        this.$message.error(this.$t('tempCarPayment.queryError'))
      }
    },

    resetCarInoutMethod() {
      this.conditions = {
        ...this.conditions,
        state: null,
        carNum: '',
        startTime: null,
        endTime: null
      }
      this.queryCarInoutMethod()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.queryCarInoutMethod()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.queryCarInoutMethod()
    },

    formatDate(date) {
      if (!date) return null
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}:${d.getSeconds().toString().padStart(2, '0')}`
    },

    getCarStatusText(status) {
      const statusMap = {
        '3306': this.$t('tempCarPayment.inStatus'),
        '100400': this.$t('tempCarPayment.paidStatus'),
        '100500': this.$t('tempCarPayment.outStatus'),
        '100600': this.$t('tempCarPayment.repayStatus')
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.left-panel {
  padding-right: 0;
}

.treeview {
  overflow-y: auto;
}

.list-group {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list-group-item {
  padding: 10px 15px;
  margin-bottom: 5px;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.list-group-item:hover {
  background-color: #f5f7fa;
}

.vc-node-selected {
  background-color: #409EFF;
  color: white;
}

.button-group {
  margin-top: 10px;
  text-align: center;
}

.box-card {
  margin-bottom: 20px;
}
</style>