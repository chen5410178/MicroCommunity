<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="4" class="text-left">
        <el-select v-model="aRoomDetailCarFeeInfo.carId" @change="changeARoomDetailCar"
          :placeholder="$t('aRoomDetailCarFee.selectCar')">
          <el-option v-for="item in aRoomDetailCarFeeInfo.ownerCars" :key="item.carId" :label="item.carNum"
            :value="item.carId">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="16" class="text-right"></el-col>
    </el-row>
    <div>
      <el-table :data="aRoomDetailCarFeeInfo.fees" style="margin-top:10px">
        <el-table-column prop="feeName" :label="$t('aRoomDetailCarFee.feeItem')" align="center">
          <template slot-scope="scope">
            <span class="hand">{{ scope.row.feeName }}</span>
            <i class="el-icon-info"></i>
          </template>
        </el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('aRoomDetailCarFee.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('aRoomDetailCarFee.feeType')" align="center"></el-table-column>
        <el-table-column prop="amountOwed" :label="$t('aRoomDetailCarFee.amount')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('aRoomDetailCarFee.createTime')" align="center"></el-table-column>
        <el-table-column :label="$t('aRoomDetailCarFee.timeRange')" align="center">
          <template slot-scope="scope">
            {{ _getEndTime(scope.row) }}~<br />{{ _getDeadlineTime(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('aRoomDetailCarFee.description')" align="center" width="150">
          <template slot-scope="scope">
            <div v-if="scope.row.feeTypeCd == '888800010015' || scope.row.feeTypeCd == '888800010016'">
              <div>{{ $t('aRoomDetailCarFee.preDegrees') }}:{{ scope.row.preDegrees }}</div>
              <div>{{ $t('aRoomDetailCarFee.curDegrees') }}:{{ scope.row.curDegrees }}</div>
              <div>{{ $t('aRoomDetailCarFee.unitPrice') }}:{{ scope.row.squarePrice }}</div>
              <div>{{ $t('aRoomDetailCarFee.additionalFee') }}:{{ scope.row.additionalAmount }}</div>
            </div>
            <div v-else-if="scope.row.feeTypeCd == '888800010017'">
              <div>{{ $t('aRoomDetailCarFee.algorithm') }}:{{ _getAttrValue(scope.row.feeAttrs, '390005') }}</div>
              <div>{{ $t('aRoomDetailCarFee.usage') }}:{{ _getAttrValue(scope.row.feeAttrs, '390003') }}</div>
            </div>
            <div v-else>
              <div>{{ $t('aRoomDetailCarFee.unitPrice') }}:{{ scope.row.squarePrice }}</div>
              <div>{{ $t('aRoomDetailCarFee.fixedFee') }}:{{ scope.row.additionalAmount }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('aRoomDetailCarFee.state')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center"></el-table-column>
      </el-table>
      <el-row>
        <el-col :span="12">
          <div>
            {{ $t('aRoomDetailCarFee.note1') }}
          </div>
          <div>
            {{ $t('aRoomDetailCarFee.note2') }}
          </div>
        </el-col>
        <el-col :span="4">
          <span> {{ $t('aRoomDetailCarFee.totalArrears') }}: {{ aRoomDetailCarFeeInfo.totalAmount }}</span>
        </el-col>
        <el-col :span="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminFee, queryAdminOwnerCars } from '@/api/aCommunity/aRoomDetailCarFeeApi'

export default {
  name: 'ARoomDetailCarFee',
  data() {
    return {
      aRoomDetailCarFeeInfo: {
        fees: [],
        ownerCars: [],
        ownerId: '',
        name: '',
        carNum: '',
        carId: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: '',
        totalAmount: 0.0
      },
      currentPage: 1,
      pageSize: 100,
      total: 0
    }
  },
  created() {
  },
  methods: {
    open(data) {
      if (!data.ownerId) return
      this.clearARoomDetailCarFeeInfo()
      Object.assign(this.aRoomDetailCarFeeInfo, data)
      this._listOwnerCar()
        .then(() => {
          this._listARoomDetailCarFee(this.currentPage, this.pageSize)
        })
        .catch(err => {
          this.$message.error(err)
        })
    },
    handleNotify() {
      this._listARoomDetailCarFee(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listARoomDetailCarFee(val, this.pageSize)
    },
    async _listARoomDetailCarFee(page, row) {
      if (!this.aRoomDetailCarFeeInfo.carId) return

      try {
        const res = await listAdminFee({
          page,
          row,
          payerObjId: this.aRoomDetailCarFeeInfo.carId
        })
        this.aRoomDetailCarFeeInfo.fees = res.fees
        this.total = res.total
        let totalAmount = 0.0
        res.fees.forEach(item => {
          totalAmount += parseFloat(item.amountOwed)
        })
        this.aRoomDetailCarFeeInfo.totalAmount = totalAmount
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _getDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return fee.deadlineTime
    },
    _getEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return fee.endTime
    },
    async _listOwnerCar() {
      return new Promise((resolve, reject) => {
        queryAdminOwnerCars({
          page: 1,
          row: 50,
          ownerId: this.aRoomDetailCarFeeInfo.ownerId,
          carTypeCd: '1001',
        })
          .then(res => {
            this.aRoomDetailCarFeeInfo.ownerCars = res.data
            if (res.data.length > 0) {
              this.aRoomDetailCarFeeInfo.carId = res.data[0].carId
              this.aRoomDetailCarFeeInfo.carNum = res.data[0].carNum
              this.aRoomDetailCarFeeInfo.num = res.data[0].num
              this.aRoomDetailCarFeeInfo.parkingName = `${res.data[0].areaNum}${this.$t('aRoomDetailCarFee.parkingLot')}${res.data[0].num}${this.$t('aRoomDetailCarFee.parkingSpace')}`
              resolve(res.data)
              return
            }
            reject(this.$t('aRoomDetailCarFee.noParkingSpace'))
          })
          .catch(err => {
            reject(err)
          })
      })
    },
    changeARoomDetailCar() {
      const car = this.aRoomDetailCarFeeInfo.ownerCars.find(item => item.carId == this.aRoomDetailCarFeeInfo.carId)
      if (!car) return

      this.aRoomDetailCarFeeInfo.carNum = car.carNum
      this.aRoomDetailCarFeeInfo.num = car.num
      this.aRoomDetailCarFeeInfo.parkingName = `${car.areaNum}${this.$t('aRoomDetailCarFee.parkingLot')}${car.num}${this.$t('aRoomDetailCarFee.parkingSpace')}`
      this._listARoomDetailCarFee(this.currentPage, this.pageSize)
    },
    clearARoomDetailCarFeeInfo() {
      this.aRoomDetailCarFeeInfo = {
        fees: [],
        ownerCars: [],
        ownerId: '',
        name: '',
        carNum: '',
        carId: '',
        total: 0,
        records: 1,
        areaNum: '',
        num: '',
        parkingName: '',
        totalAmount: 0.0
      }
    },
    _simplifyCarGetFeeOwnerInfo(attrs) {
      const ownerName = this._getAttrValue(attrs, '390008')
      const ownerLink = this._getAttrValue(attrs, '390009')
      return `${this.$t('aRoomDetailCarFee.owner')}:${ownerName},${this.$t('aRoomDetailCarFee.phone')}:${ownerLink}`
    },
    _getAttrValue(attrs, attrId) {
      if (!attrs) return ''
      const attr = attrs.find(item => item.attrId == attrId)
      return attr ? attr.value : ''
    }
  }
}
</script>

<style scoped>
.hand {
  cursor: pointer;
}

/* Other styles same as previous components */
</style>