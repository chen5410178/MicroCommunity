<template>
  <div class="margin-top">
    <el-row class="margin-top-lg">
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-input v-model="aRoomDetailCarInfo.carNum" type="text"
          :placeholder="$t('aRoomDetailCar.placeholderCarNum')"></el-input>
      </el-col>
      <el-col :span="4" class="padding-right-xs padding-right-xl">
        <el-button type="primary" size="small" style="margin-left:10px" @click="_qureyARoomDetailCar()">
          <i class="el-icon-search"></i>{{ $t('common.search') }}
        </el-button>
      </el-col>
      <el-col :span="16" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="aRoomDetailCarInfo.cars" style="width: 100%">
        <el-table-column prop="carNum" :label="$t('aRoomDetailCar.carNum')" align="center"></el-table-column>
        <el-table-column prop="leaseType" :label="$t('aRoomDetailCar.leaseType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.leaseType == 'T' ? $t('aRoomDetailCar.tempCar') : scope.row.leaseTypeName }}
          </template>
        </el-table-column>
        <el-table-column prop="carTypeName" :label="$t('aRoomDetailCar.carType')" align="center"></el-table-column>
        <el-table-column prop="carColor" :label="$t('aRoomDetailCar.color')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('aRoomDetailCar.owner')" align="center">
          <template slot-scope="scope">{{ scope.row.ownerName }}({{ scope.row.link }})</template>
        </el-table-column>
        <el-table-column prop="areaNum" :label="$t('aRoomDetailCar.parkingSpace')" align="center">
          <template slot-scope="scope">
            {{ scope.row.areaNum && scope.row.state == '1001' ? `${scope.row.areaNum}-${scope.row.num}` :
              $t('aRoomDetailCar.released')}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('aRoomDetailCar.validity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
          </template>
        </el-table-column>
      </el-table>
      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryAdminOwnerCars } from '@/api/aCommunity/aRoomDetailCarApi'

export default {
  name: 'ARoomDetailCar',
  data() {
    return {
      aRoomDetailCarInfo: {
        cars: [],
        ownerId: '',
        ownerName: '',
        carNum: '',
        memberId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
  },
  methods: {
    open(data) {
      this.aRoomDetailCarInfo.ownerId = data.ownerId
      this.aRoomDetailCarInfo.memberId = data.carId
      this.aRoomDetailCarInfo.ownerName = data.ownerName
      this._loadARoomDetailCarData(this.currentPage, this.pageSize)
    },
    handleSwitch(data) {
      this.aRoomDetailCarInfo.ownerId = data.ownerId
      this.aRoomDetailCarInfo.memberId = data.carId
      this.aRoomDetailCarInfo.ownerName = data.ownerName
      this._loadARoomDetailCarData(this.currentPage, this.pageSize)
    },
    handleNotify() {
      this._loadARoomDetailCarData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadARoomDetailCarData(val, this.pageSize)
    },
    async _loadARoomDetailCarData(page, row) {
      try {
        const res = await queryAdminOwnerCars({
          ownerId: this.aRoomDetailCarInfo.ownerId,
          carNum: this.aRoomDetailCarInfo.carNum,
          memberId: this.aRoomDetailCarInfo.memberId,
          page,
          row
        })
        this.aRoomDetailCarInfo.cars = res.data
        this.total = res.total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _qureyARoomDetailCar() {
      this.currentPage = 1
      this._loadARoomDetailCarData(1, this.pageSize)
    }
  }
}
</script>

<style scoped>
/* Same styles as aRoomDetailMember */
</style>