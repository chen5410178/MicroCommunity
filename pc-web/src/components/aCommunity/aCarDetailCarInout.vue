<template>
  <div class="car-inout-container">
    <el-row>
      <el-col :span="24" class="text-right">
        <!-- 可以在这里添加操作按钮 -->
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table
        :data="aCarDetailCarInoutInfo.carIns"
        border
        style="width: 100%"
        class="footable"
      >
        <el-table-column prop="photoJpg" :label="$t('aCarDetailCarInout.entryImage')" align="center">
          <template slot-scope="scope">
            <img
              style="width: 60px; height: 60px;"
              class="border-radius"
              :src="scope.row.photoJpg || '/img/noPhoto.jpg'"
              @click="_carInoutOpenFile(scope.row.photoJpg)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="inoutId" :label="$t('aCarDetailCarInout.inoutNumber')" align="center" />
        <el-table-column :label="$t('aCarDetailCarInout.vehicleStatus')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carInout == '3306' ? $t('aCarDetailCarInout.entry') : $t('aCarDetailCarInout.exit') }}({{ scope.row.stateName }})
          </template>
        </el-table-column>
        <el-table-column prop="carNum" :label="$t('aCarDetailCarInout.plateNumber')" align="center" />
        <el-table-column prop="paNum" :label="$t('aCarDetailCarInout.parkingLot')" align="center" />
        <el-table-column :label="$t('aCarDetailCarInout.billingRule')" align="center">
          <template slot-scope="scope">
            <span @click="_viewTempFeeConfigInOutCar(scope.row.configId)" style="cursor: pointer;">
              {{ scope.row.feeName }}
              <i class="el-icon-info"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="carTypeName" :label="$t('aCarDetailCarInout.plateType')" align="center" />
        <el-table-column prop="inTime" :label="$t('aCarDetailCarInout.entryTime')" align="center" />
        <el-table-column :label="$t('aCarDetailCarInout.exitTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carInout != '3307' ? '-' : scope.row.outTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('aCarDetailCarInout.parkingTime')" align="center">
          <template slot-scope="scope">
            {{ scope.row.hours }}{{ $t('aCarDetailCarInout.hour') }}{{ scope.row.min }}{{ $t('aCarDetailCarInout.minute') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('aCarDetailCarInout.chargeAmount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.carType != 'T' ? '-' : scope.row.payCharge }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('aCarDetailCarInout.remark')" align="center" />
      </el-table>

      <el-row class="margin-top">
        <el-col :span="8" :offset="16">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getCarInoutDetail } from '@/api/aCommunity/aCarDetailCarInoutApi'

export default {
  name: 'ACarDetailCarInout',
  data() {
    return {
      aCarDetailCarInoutInfo: {
        carIns: [],
        carId: '',
        memberId: '',
        carNum: '',
        areaNum: ''
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
      this.aCarDetailCarInoutInfo.carId = data.carId
      this.aCarDetailCarInoutInfo.carNum = data.carNum
      this.aCarDetailCarInoutInfo.areaNum = data.areaNum
      this.aCarDetailCarInoutInfo.memberId = data.memberId
      this._loadACarDetailCarInoutData(this.currentPage, this.pageSize)
    },
    handleNotify() {
      this._loadACarDetailCarInoutData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadACarDetailCarInoutData(val, this.pageSize)
    },
    _loadACarDetailCarInoutData(page, row) {
      const params = {
        carNum: this.aCarDetailCarInoutInfo.carNum,
        paNum: this.aCarDetailCarInoutInfo.areaNum,
        iotApiCode: 'listCarInoutDetailBmoImpl',
        page: page,
        row: row
      }

      getCarInoutDetail(params)
        .then(response => {
          const res = response.data
          this.aCarDetailCarInoutInfo.carIns = res.data
          this.total = res.total
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },
    _carInoutOpenFile(photoJpg) {
      if (photoJpg) {
        // 这里实现打开图片的逻辑
        console.log('打开图片:', photoJpg)
      }
    },
    _viewTempFeeConfigInOutCar(configId) {
      // 这里实现查看计费规则的逻辑
      console.log('查看计费规则:', configId)
    }
  }
}
</script>

<style scoped>
.car-inout-container {
  padding: 20px;
}
.margin-top {
  margin-top: 20px;
}
.border-radius {
  border-radius: 4px;
}
.text-right {
  text-align: right;
}
</style>