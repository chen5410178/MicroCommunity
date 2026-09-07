<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" style="margin-left:10px" @click="_buyMonthCard()">
          {{ $t('carDetailFee.createFee') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="carDetailFeeInfo.fees" style="width: 100%">
        <el-table-column prop="feeName" :label="$t('carDetailFee.feeItem')" align="center">
          <template #default="{ row }">
            <span class="hand" @click="_viewCarFeeConfig(row)">{{ row.feeName }}
              <i class="el-icon-info"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('carDetailFee.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('carDetailFee.feeType')" align="center"></el-table-column>
        <el-table-column prop="amountOwed" :label="$t('carDetailFee.amountReceivable')"
          align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('carDetailFee.accountingTime')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailFee.receivablePeriod')" align="center">
          <template #default="{ row }">
            {{ _getEndTime(row) }}~<br />{{ _getDeadlineTime(row) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailFee.remark')" align="center">
          <template #default="{ row }">
            <div v-if="row.feeTypeCd == '888800010015' || row.feeTypeCd == '888800010016'">
              <div>
                <span>{{ $t('carDetailFee.previousReading') }}</span>：{{ row.preDegrees }}
              </div>
              <div>
                <span>{{ $t('carDetailFee.currentReading') }}</span>：{{ row.curDegrees }}
              </div>
              <div>
                <span>{{ $t('carDetailFee.unitPrice') }}</span>：{{ row.mwPrice ? row.mwPrice : row.squarePrice }}
              </div>
              <div>
                <span>{{ $t('carDetailFee.additionalFee') }}</span>：{{ row.additionalAmount }}
              </div>
            </div>
            <div v-else>
              <div>
                <span>{{ $t('carDetailFee.unitPrice') }}</span>：{{ row.squarePrice }}
              </div>
              <div>
                <span>{{ $t('carDetailFee.fixedFee') }}</span>：{{ row.additionalAmount }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('carDetailFee.status')" align="center"></el-table-column>
        <el-table-column :label="$t('carDetailFee.operation')" align="center">
          <template #default="{ row }">
            <el-button type="text" v-if="row.state != '2009001' && hasPrivilege('502020082314267912')"
              @click="_payFee(row)">
              {{ $t('carDetailFee.payment') }}
            </el-button>
            <el-button type="text" @click="_payFeeHis(row)">
              {{ $t('carDetailFee.history') }}
            </el-button>
            <el-button type="text" v-if="hasPrivilege('502020090604200029')" @click="_deleteFee(row)">
              {{ $t('carDetailFee.cancel') }}
            </el-button>
            <el-button type="text" v-if="row.state != '2009001' && hasPrivilege('502020090427190001')"
              @click="_editFee(row)">
              {{ $t('carDetailFee.change') }}
            </el-button>
            <el-button type="text" @click="_viewCarFee(row)">
              {{ $t('carDetailFee.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12">
          <span>{{ $t('carDetailFee.paymentNotice') }}</span>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" :total="pagination.total" layout="prev, pager, next, jumper">
          </el-pagination>
        </el-col>
      </el-row>
    </div>

    <add-meter-water ref="addMeterWater" @success="notify"></add-meter-water>
    <delete-fee ref="deleteFee" @success="notify"></delete-fee>
    <edit-fee ref="editFee" @success="notify"></edit-fee>
  </div>
</template>

<script>
import { listFee } from '@/api/car/carDetailFeeApi'
import AddMeterWater from '@/components/fee/addMeterWater'
import DeleteFee from '@/components/fee/deleteFee'
import EditFee from '@/components/fee/editFee'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CarDetailFee',
  components: {
    AddMeterWater,
    DeleteFee,
    EditFee
  },
  data() {
    return {
      carDetailFeeInfo: {
        fees: [],
        carId: '',
        memberId: '',
        carNum: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.carDetailFeeInfo.carId = data.carId
      this.carDetailFeeInfo.carNum = data.carNum
      this.carDetailFeeInfo.memberId = data.memberId
      this._loadCarDetailFeeData(1, this.pagination.pageSize)
    },
    switch(data) {
      this.carDetailFeeInfo.carId = data.carId
      this.carDetailFeeInfo.carNum = data.carNum
      this.carDetailFeeInfo.memberId = data.memberId
      this._loadCarDetailFeeData(1, this.pagination.pageSize)
    },
    notify() {
      this._loadCarDetailFeeData(this.pagination.currentPage, this.pagination.pageSize)
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadCarDetailFeeData(val, this.pagination.pageSize)
    },
    _loadCarDetailFeeData(page, row) {
      const params = {
        communityId: this.communityId,
        payerObjId: this.carDetailFeeInfo.carId,
        payerObjType: '5555',
        page: page,
        row: row
      }

      listFee(params).then(response => {
        this.carDetailFeeInfo.fees = response.fees
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _payFee(fee) {
      fee.roomName = this.carDetailFeeInfo.carNum
      this.$router.push(`/views/fee/payFeeOrder?feeId=${fee.feeId}`)
    },
    _payFeeHis(fee) {
      this.$router.push(`/views/fee/propertyFee?${this.objToQueryString(fee)}`)
    },
    _editFee(fee) {
      this.$refs.editFee.open(fee)
    },
    _deleteFee(fee) {
      this.$refs.deleteFee.open({
        communityId: this.communityId,
        feeId: fee.feeId
      })
    },
    _buyMonthCard() {
      this.$router.push(`/views/fee/buyCarMonthCard?carNum=${this.carDetailFeeInfo.carNum}`)
    },
    _openAddMeterWaterModal() {
      this.$refs.addMeterWater.open({
        roomId: this.carDetailFeeInfo.carId,
        roomName: this.carDetailFeeInfo.carNum,
        ownerName: this.carDetailFeeInfo.parkingName,
        objType: '6666'
      })
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
    _viewCarFeeConfig(fee) {
      // 实现查看费用配置逻辑
      //window.open(`/#/views/fee/feeDetailConfig?configId=${fee.configId}`)
      console.log(fee)
    },
    _viewCarFee(fee) {
      // 实现查看费用详情逻辑
      window.open(`/#/views/fee/feeDetail?feeId=${fee.feeId}`)
    },
    objToQueryString(obj) {
      return Object.keys(obj).map(key => `${key}=${obj[key]}`).join('&')
    },
  }
}
</script>

<style scoped>
.hand {
  cursor: pointer;
}

.text-right {
  text-align: right;
}

.margin-top {
  margin-top: 20px;
}
</style>