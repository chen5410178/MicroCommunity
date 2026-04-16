<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="aCarDetailHisInfo.cars" style="width: 100%">
        <el-table-column prop="carNum" :label="$t('carDetailHis.carNum')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailHis.leaseType')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.leaseType == 'T'">{{$t('carDetailHis.tempCar')}}</span>
            <span v-else>{{scope.row.leaseTypeName}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="carTypeName" :label="$t('carDetailHis.carType')" align="center"></el-table-column>
        <el-table-column prop="carColor" :label="$t('carDetailHis.color')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailHis.owner')" align="center">
          <template slot-scope="scope">
            <div class="hand">{{scope.row.ownerName}}({{scope.row.link}})</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailHis.parkingSpace')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.areaNum && scope.row.state == '1001'">
              {{scope.row.areaNum}}-{{scope.row.num}}
            </span>
            <span v-else>{{$t('carDetailHis.spaceReleased')}}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailHis.validPeriod')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.leaseType == 'H'">
              {{scope.row.startTime}}<br>~{{scope.row.endTime}}
            </span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailHis.action')" align="center">
          <template slot-scope="scope">
            {{_getHisOperate(scope.row)}}
          </template>
        </el-table-column>
        <el-table-column prop="userName" :label="$t('carDetailHis.operator')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('carDetailHis.operateTime')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12" class="text-right">
          <el-pagination
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryAdminHisOwnerCar } from '@/api/aCommunity/aCarDetailHisApi'

export default {
  name: 'ACarDetailHis',
  data() {
    return {
      aCarDetailHisInfo: {
        cars: [],
        carId: '',
        memberId: '',
        carNum: '',
        carNumLike: '',
        logStartTime: '',
        logEndTime: '',
        paId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.handleSwitch(data)
    },
    handleSwitch(data) {
      this.aCarDetailHisInfo.carId = data.carId
      this.aCarDetailHisInfo.carNum = data.carNum
      this.aCarDetailHisInfo.paId = data.paId
      this.aCarDetailHisInfo.memberId = data.memberId
      this.aCarDetailHisInfo.carNumLike = data.carNumLike
      this.aCarDetailHisInfo.logStartTime = data.logStartTime
      this.aCarDetailHisInfo.logEndTime = data.logEndTime
      this.aCarDetailHisInfo.staffNameLike = data.staffNameLike
      this._loadACarDetailHisData(1, this.pageSize)
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadACarDetailHisData(page, this.pageSize)
    },
    async _loadACarDetailHisData(page, row) {
      try {
        const param = {
          carNum: this.aCarDetailHisInfo.carNum,
          carNumLike: this.aCarDetailHisInfo.carNumLike,
          logStartTime: this.aCarDetailHisInfo.logStartTime,
          logEndTime: this.aCarDetailHisInfo.logEndTime,
          staffNameLike: this.aCarDetailHisInfo.staffNameLike,
          page,
          row
        }
        const response = await queryAdminHisOwnerCar(param)
        this.aCarDetailHisInfo.cars = response.data
        this.total = response.total
      } catch (error) {
        console.error('Failed to load history data:', error)
      }
    },
    _getHisOperate(car) {
      let carCount = 0
      this.aCarDetailHisInfo.cars.forEach(item => {
        if (car.bId == item.bId) {
          carCount += 1
        }
      })
      if (carCount <= 1) {
        if (car.operate == 'ADD') {
          return this.$t('carDetailHis.add')
        }
        if (car.operate == 'DEL') {
          return this.$t('carDetailHis.delete')
        }
        return "-"
      }
      if (car.operate == 'ADD') {
        return this.$t('carDetailHis.modifyNew')
      }
      if (car.operate == 'DEL') {
        return this.$t('carDetailHis.modifyOld')
      }
      return "-"
    }
  }
}
</script>

<style scoped>
.hand {
  cursor: pointer;
}
.margin-top {
  margin-top: 20px;
}
.text-right {
  text-align: right;
}
</style>