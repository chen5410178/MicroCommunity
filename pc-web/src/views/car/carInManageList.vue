<template>
  <div class="animated fadeInRight car-in-manage-container">
    <el-row :gutter="20">
      <!-- 左侧停车场区域选择 -->

      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li v-for="(item, index) in carInManageInfo.parkingAreas" :key="index"
                class="parking-area-item"
                :class="{ 'selected': carInManageInfo.conditions.paNum === item.num }" @click="swatchParkingArea(item)">
                {{ item.num }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧内容区域 -->
      <el-col :span="21" class="right-panel">
        <!-- 查询条件卡片 -->
        <el-card class="search-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('carInManage.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input :placeholder="$t('carInManage.inputPlateNumber')" v-model="carInManageInfo.conditions.carNum"
                clearable @keyup.enter.native="queryCarInoutMethod" />
            </el-col>

            <el-col :span="6">
              <el-select v-model="carInManageInfo.conditions.state" :placeholder="$t('carInManage.selectVehicleStatus')"
                style="width:100%">
                <el-option value="" :label="$t('carInManage.selectVehicleStatus')" />
                <el-option value="100300" :label="$t('carInManage.inStatus')" />
                <el-option value="100400" :label="$t('carInManage.paymentComplete')" />
                <el-option value="100500" :label="$t('carInManage.exitStatus')" />
                <el-option value="100600" :label="$t('carInManage.timeoutRepayment')" />
              </el-select>
            </el-col>

            <el-col :span="6">
              <el-select v-model="carInManageInfo.conditions.carType" :placeholder="$t('carInManage.selectPlateType')"
                style="width:100%">
                <el-option value="" :label="$t('carInManage.selectPlateType')" />
                <el-option value="T" :label="$t('carInManage.temporaryVehicle')" />
                <el-option value="S" :label="$t('carInManage.forSaleVehicle')" />
                <el-option value="H" :label="$t('carInManage.monthlyRental')" />
                <el-option value="W" :label="$t('carInManage.whitelist')" />
                <el-option value="B" :label="$t('carInManage.blacklist')" />
                <el-option value="I" :label="$t('carInManage.internalVehicle')" />
                <el-option value="NM" :label="$t('carInManage.freeVehicle')" />
              </el-select>
            </el-col>

            <el-col :span="6">
              <el-date-picker v-model="carInManageInfo.conditions.startTime" type="datetime"
                :placeholder="$t('carInManage.enterStartTime')" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
            </el-col>

            <el-col :span="6" class="margin-top-10">
              <el-date-picker v-model="carInManageInfo.conditions.endTime" type="datetime"
                :placeholder="$t('carInManage.enterEndTime')" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
            </el-col>

            <el-col :span="6" class="margin-top-10">
              <el-button type="primary" @click="queryCarInoutMethod">
                {{ $t('carInManage.search') }}
              </el-button>
              <el-button @click="resetCarInoutMethod">
                {{ $t('carInManage.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <!-- 在场车辆卡片 -->
        <el-card class="list-card margin-top-20">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('carInManage.parkedVehicles') }}</div>
          </div>

          <el-table :data="carInManageInfo.carIns" border style="width: 100%" v-loading="loading">
            <el-table-column prop="ciId" :label="$t('carInManage.inoutId')" align="center" />
            <el-table-column prop="stateName" :label="$t('carInManage.vehicleStatus')" align="center" />
            <el-table-column prop="carNum" :label="$t('carInManage.plateNumber')" align="center" />
            <el-table-column prop="paNum" :label="$t('carInManage.parkingLot')" align="center" />
            <el-table-column prop="feeName" :label="$t('carInManage.billingRule')" align="center" />
            <el-table-column prop="carTypeName" :label="$t('carInManage.plateType')" align="center" />
            <el-table-column prop="inTime" :label="$t('carInManage.entryTime')" align="center" />
            <el-table-column :label="$t('carInManage.parkingDuration')" align="center">
              <template slot-scope="scope">
                {{ scope.row.hours }} {{ $t('carInManage.hour') }} {{ scope.row.min }} {{ $t('carInManage.minute') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('carInManage.parkingFee')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.carType !== '1001'">{{ scope.row.payCharge }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <el-pagination class="margin-top-20" :current-page.sync="pagination.currentPage" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listParkingAreas, listCarIns } from '@/api/car/carInManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarInManageList',
  data() {
    return {
      loading: false,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      carInManageInfo: {
        parkingAreas: [],
        carIns: [],
        conditions: {
          state: '',
          carNum: '',
          carType: '',
          startTime: '',
          endTime: '',
          paNum: '',
          iotApiCode: 'listCarInParkingAreaBmoImpl',
          page: 1,
          row: 10,
          communityId: ''
        }
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.carInManageInfo.conditions.communityId = this.communityId
    this.loadParkingAreas()
  },
  methods: {
    // 加载停车场区域
    async loadParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const response = await listParkingAreas(params)
        this.carInManageInfo.parkingAreas = response.parkingAreas || []

        if (this.carInManageInfo.parkingAreas.length > 0) {
          this.carInManageInfo.conditions.paNum = this.carInManageInfo.parkingAreas[0].num
          this.loadCarIns()
        }
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      }
    },
    swatchParkingArea(parkingArea) {
      this.carInManageInfo.conditions.paNum = parkingArea.num
      this.loadCarIns()
    },

    // 加载在场车辆
    async loadCarIns() {
      this.loading = true
      try {
        // 处理参数
        const params = {
          ...this.carInManageInfo.conditions,
          page: this.pagination.currentPage,
          row: this.pagination.pageSize
        }
        params.carNum = params.carNum ? params.carNum.trim() : ''

        // 调用API
        const response = await listCarIns(params)
        this.carInManageInfo.carIns = response.data || []
        this.pagination.total = response.total || 0
      } catch (error) {
        this.$message.error(this.$t('common.loadFailed'))
      } finally {
        this.loading = false
      }
    },

    // 切换停车场区域
    handleAreaChange(val) {
      this.carInManageInfo.conditions.paNum = val
      this.loadCarIns()
    },

    // 查询
    queryCarInoutMethod() {
      this.pagination.currentPage = 1
      this.loadCarIns()
    },

    // 重置
    resetCarInoutMethod() {
      this.carInManageInfo.conditions = {
        ...this.carInManageInfo.conditions,
        state: '',
        carNum: '',
        carType: '',
        startTime: '',
        endTime: ''
      }
      this.pagination.currentPage = 1
      this.loadCarIns()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadCarIns()
    },

    // 当前页改变
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadCarIns()
    }
  }
}
</script>

<style lang="scss" scoped>
.car-in-manage-container {
  padding: 20px;

  .right-panel {
    padding-left: 5px;
  }

  .left-panel {
    .parking-area-card {
      height: 700px;

      .parking-area-list-wrapper {
        height: 630px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #dcdfe6;
          border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        .parking-area-list {
          list-style: none;
          padding: 0 5px;
          margin: 0;

          .parking-area-item {
            padding: 10px;
            text-align: center;
            cursor: pointer;
            border-radius: 4px;
            margin-bottom: 5px;
            transition: all 0.3s;
            font-size: 14px;
            color: #606266;

            &:hover {
              background-color: #f5f7fa;
            }

            &.selected {
              background-color: #409eff;
              color: white;
            }
          }
        }
      }
    }
  }

  .search-card,
  .list-card {
    border-radius: 5px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
  }

  .margin-top-10 {
    margin-top: 10px;
  }

  .margin-top-20 {
    margin-top: 20px;
  }
}
</style>