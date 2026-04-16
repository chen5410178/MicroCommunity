<template>
  <div class="owner-detail-room-fee">
    <div class="margin-top">
      <el-row class="margin-top-lg">
        <el-col :span="16">
          <span class="margin-right" v-for="(item, index) in aOwnerDetailRoomFeeInfo.payObjs" :key="index">
            <el-checkbox class="checkRoomItem" :value="item.roomId" v-model="aOwnerDetailRoomFeeInfo.payerObjIds"
              @change="_chanagePayerObjId">
              {{ item.floorNum }}-{{ item.unitNum }}-{{ item.roomNum }}
            </el-checkbox>
          </span>
        </el-col>
        <el-col :span="8">
          <el-select v-model="aOwnerDetailRoomFeeInfo.state" @change="_changeContractConfigId" size="small">
            <el-option :label="$t('aOwnerDetailRoomFee.selectStatus')" value="" />
            <el-option :label="$t('aOwnerDetailRoomFee.valid')" value="2008001" />
            <el-option :label="$t('aOwnerDetailRoomFee.chargeEnd')" value="2009001" />
          </el-select>
        </el-col>
      </el-row>

      <div class="margin-top">
        <el-table :data="aOwnerDetailRoomFeeInfo.fees" border style="width: 100%">
          <el-table-column prop="payerObjName" :label="$t('aOwnerDetailRoomFee.room')" align="center" />
          <el-table-column prop="feeName" :label="$t('aOwnerDetailRoomFee.feeItem')" align="center" />
          <el-table-column prop="feeFlagName" :label="$t('aOwnerDetailRoomFee.feeFlag')" align="center" />
          <el-table-column prop="feeTypeCdName" :label="$t('aOwnerDetailRoomFee.feeType')" align="center" />
          <el-table-column prop="amountOwed" :label="$t('aOwnerDetailRoomFee.amountReceivable')" align="center" />
          <el-table-column prop="startTime" :label="$t('aOwnerDetailRoomFee.accountTime')" align="center" />
          <el-table-column :label="$t('aOwnerDetailRoomFee.receivablePeriod')" align="center">
            <template #default="{ row }">
              {{ _getRoomEndTime(row) }}~<br>{{ _getRoomDeadlineTime(row) }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('aOwnerDetailRoomFee.description')" align="center">
            <template #default="{ row }">
              <div v-if="row.computingFormula === '5005' || row.computingFormula === '9009'">
                <div>{{ $t('aOwnerDetailRoomFee.lastDegree') }}: {{ row.preDegrees }}</div>
                <div>{{ $t('aOwnerDetailRoomFee.currentDegree') }}: {{ row.curDegrees }}</div>
                <div>{{ $t('aOwnerDetailRoomFee.unitPrice') }}: {{ row.mwPrice ? row.mwPrice : row.squarePrice }}</div>
                <div>{{ $t('aOwnerDetailRoomFee.additionalFee') }}: {{ row.additionalAmount }}</div>
              </div>
              <div v-else-if="row.computingFormula === '6006'">
                <div>{{ $t('aOwnerDetailRoomFee.usage') }}: {{ _getRoomAttrValue(row.feeAttrs, '390006') }}</div>
                <div>{{ $t('aOwnerDetailRoomFee.unitPrice') }}: {{ row.squarePrice }}</div>
                <div>{{ $t('aOwnerDetailRoomFee.additionalFee') }}: {{ row.additionalAmount }}</div>
              </div>
              <div v-else-if="row.feeTypeCd === '888800010017'" style="width: 150px">
                <div>{{ $t('aOwnerDetailRoomFee.algorithm') }}: {{ _getRoomAttrValue(row.feeAttrs, '390005') }}</div>
                <div>{{ $t('aOwnerDetailRoomFee.usage') }}: {{ _getRoomAttrValue(row.feeAttrs, '390003') }}</div>
              </div>
              <div v-else-if="row.computingFormula === '4004'" style="width: 150px">
                <div>{{ $t('aOwnerDetailRoomFee.feeBasedOnActual') }}</div>
              </div>
              <div v-else>
                <div>{{ $t('aOwnerDetailRoomFee.unitPrice') }}: {{ row.squarePrice }}</div>
                <div v-if="row.feeFlag === '1003006'">{{ $t('aOwnerDetailRoomFee.additionalFee') }}:
                  {{ row.additionalAmount }}</div>
                <div v-else>{{ $t('aOwnerDetailRoomFee.fixedFee') }}: {{ row.additionalAmount }}</div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="stateName" :label="$t('aOwnerDetailRoomFee.status')" align="center" />
          <el-table-column :label="$t('aOwnerDetailRoomFee.operation')" align="center" />
        </el-table>

        <el-row class="margin-top">
          <el-col :span="12">
            <div>{{ $t('aOwnerDetailRoomFee.note1') }}</div>
            <div>{{ $t('aOwnerDetailRoomFee.note2') }}</div>
          </el-col>
          <el-col :span="4">
            <span>{{ $t('aOwnerDetailRoomFee.arrearsSubtotal') }}</span>
            {{ aOwnerDetailRoomFeeInfo.totalAmount }}
          </el-col>
          <el-col :span="8">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
              layout="total, prev, pager, next" :total="total" />
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { listAdminFee,queryAdminOwnerRooms } from '@/api/aCommunity/aOwnerDetailRoomFeeApi'
//import { getCommunityId } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'AOwnerDetailRoomFee',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 30,
      aOwnerDetailRoomFeeInfo: {
        fees: [],
        roomNum: '',
        allOweFeeAmount: '0',
        payObjs: [],
        payerObjIds: [],
        ownerId: '',
        state: '2008001',
        totalAmount: 0
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aOwnerDetailRoomFeeInfo.ownerId = data.ownerId
      this._loadDetailRoomsData()
    },
    handleNotify() {
      this._loadAOwnerDetailRoomFeeData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadAOwnerDetailRoomFeeData(val, this.DEFAULT_ROWS)
    },
    async _loadAOwnerDetailRoomFeeData(page, row) {
      const payerObjIds = this.aOwnerDetailRoomFeeInfo.payerObjIds.join(',')
      const param = {
        page,
        row,
        payerObjIds,
        state: this.aOwnerDetailRoomFeeInfo.state
      }

      try {
        const response = await listAdminFee(param)
        this.total = response.total
        this.aOwnerDetailRoomFeeInfo.fees = response.fees
        this.currentPage = page

        let totalAmount = 0.0
        this.aOwnerDetailRoomFeeInfo.fees.forEach(item => {
          totalAmount += parseFloat(item.amountOwed)
        })
        this.aOwnerDetailRoomFeeInfo.totalAmount = totalAmount.toFixed(2)
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _getRoomAttrValue(attrs, specCd) {
      let value = ""
      attrs.forEach(item => {
        if (item.specCd === specCd) {
          value = item.value
          return
        }
      })
      return value
    },
    _getRoomDeadlineTime(fee) {
      if (fee.amountOwed === 0 && fee.endTime === fee.deadlineTime) {
        return "-"
      }
      if (fee.state === '2009001') {
        return "-"
      }
      return dateFormat(fee.deadlineTime)
    },
    _getRoomEndTime(fee) {
      if (fee.state === '2009001') {
        return "-"
      }
      return dateFormat(fee.endTime)
    },
    _chanagePayerObjId() {
      this._loadAOwnerDetailRoomFeeData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    _changeContractConfigId() {
      this._loadAOwnerDetailRoomFeeData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    async _loadDetailRoomsData() {
      const param = {
        ownerId: this.aOwnerDetailRoomFeeInfo.ownerId,
        page: 1,
        row: 100
      }
      this.aOwnerDetailRoomFeeInfo.payerObjIds = []

      try {
        const response = await queryAdminOwnerRooms(param)
        this.aOwnerDetailRoomFeeInfo.payObjs = response.rooms
        response.rooms.forEach(room => {
          this.aOwnerDetailRoomFeeInfo.payerObjIds.push(room.roomId)
        })
        this._loadAOwnerDetailRoomFeeData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
      } catch (error) {
        console.error('请求失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}

.margin-right {
  margin-right: 15px;
}
</style>