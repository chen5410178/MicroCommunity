<template>
  <div class="">
    <div class="flex justify-end">
      <div  class="padding-right-xs padding-left-xl">
        <span class="margin-right" v-for="(item, index) in ownerDetailRoomFeeInfo.payObjs" :key="index">
          <el-checkbox class="checkRoomItem" v-model="ownerDetailRoomFeeInfo.payerObjIds" :label="item.roomId"
            @change="_chanagePayerObjId()">
            {{ item.floorNum }}-{{ item.unitNum }}-{{ item.roomNum }}
          </el-checkbox>
        </span>
      </div>
      <div  class="padding-lr-xs">
        <el-select v-model="ownerDetailRoomFeeInfo.state" @change="_changeContractConfigId()" size="small"
          :placeholder="$t('aOwnerDetailRoomFee.selectStatus')">
          <el-option :label="$t('aOwnerDetailRoomFee.valid')" value="2008001"></el-option>
          <el-option :label="$t('aOwnerDetailRoomFee.feeEnd')" value="2009001"></el-option>
        </el-select>
      </div>
      <div :span="9" class="text-right">
        
      </div>
    </div>
    <div class="margin-top-sm">
      <el-table :data="ownerDetailRoomFeeInfo.fees" style="width: 100%">
        <el-table-column prop="payerObjName" :label="$t('aOwnerDetailRoomFee.room')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('aOwnerDetailRoomFee.feeItem')" align="center"></el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('aOwnerDetailRoomFee.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="feeTypeCdName" :label="$t('aOwnerDetailRoomFee.feeType')"
          align="center"></el-table-column>
        <el-table-column prop="amountOwed" :label="$t('aOwnerDetailRoomFee.amountReceivable')" align="center"></el-table-column>
        <el-table-column prop="startTime" :label="$t('aOwnerDetailRoomFee.accountTime')" align="center"></el-table-column>
        <el-table-column :label="$t('aOwnerDetailRoomFee.receivablePeriod')" align="center">
          <template slot-scope="scope">
            {{ _getRoomEndTime(scope.row) }}~<br>{{ _getRoomDeadlineTime(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('aOwnerDetailRoomFee.description')" align="center" width="150">
          <template slot-scope="scope">
            <div v-if="scope.row.computingFormula == '5005' || scope.row.computingFormula == '9009'">
              <div><span>{{ $t('aOwnerDetailRoomFee.lastDegree') }}</span>{{ scope.row.preDegrees }}</div>
              <div><span>{{ $t('aOwnerDetailRoomFee.currentDegree') }}</span>{{ scope.row.curDegrees }}</div>
              <div><span>{{ $t('aOwnerDetailRoomFee.unitPrice')
                  }}</span>{{ scope.row.mwPrice ? scope.row.mwPrice : scope.row.squarePrice }}</div>
              <div><span>{{ $t('aOwnerDetailRoomFee.additionalFee') }}</span>{{ scope.row.additionalAmount }}</div>
            </div>
            <div v-else-if="scope.row.computingFormula == '6006'">
              <div><span>{{ $t('aOwnerDetailRoomFee.usage') }}</span>{{ _getRoomAttrValue(scope.row.feeAttrs, '390006') }}
              </div>
              <div><span>{{ $t('aOwnerDetailRoomFee.unitPrice') }}</span>{{ scope.row.squarePrice }}</div>
              <div><span>{{ $t('aOwnerDetailRoomFee.additionalFee') }}</span>{{ scope.row.additionalAmount }}</div>
            </div>
            <div v-else-if="scope.row.feeTypeCd == '888800010017'" width="150">
              <div><span>{{ $t('aOwnerDetailRoomFee.algorithm')
                  }}</span>{{ _getRoomAttrValue(scope.row.feeAttrs, '390005') }}</div>
              <div><span>{{ $t('aOwnerDetailRoomFee.usage') }}</span>{{ _getRoomAttrValue(scope.row.feeAttrs, '390003') }}
              </div>
            </div>
            <div v-else-if="scope.row.computingFormula == '4004'">
              <div>{{ $t('aOwnerDetailRoomFee.feeByActual') }}</div>
            </div>
            <div v-else>
              <div><span>{{ $t('aOwnerDetailRoomFee.unitPrice') }}</span>{{ scope.row.squarePrice }}</div>
              <div v-if="scope.row.feeFlag == '1003006'"><span>{{ $t('aOwnerDetailRoomFee.additionalFee')
                  }}</span>{{ scope.row.additionalAmount }}</div>
              <div v-else><span>{{ $t('aOwnerDetailRoomFee.fixedFee') }}</span>{{ scope.row.additionalAmount }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('aOwnerDetailRoomFee.status')" align="center"></el-table-column>
        <!-- <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="text" size="mini"
                v-if="scope.row.state != '2009001' && hasPrivilege('502020082314267912')"
                @click="_payRoomFee(scope.row)">
                {{ $t('ownerDetailRoomFee.pay') }}
              </el-button>
              <el-button type="text" size="mini" @click="_payRoomFeeHis(scope.row)">
                {{ $t('ownerDetailRoomFee.payHistory') }}
              </el-button>
              <el-button type="text" size="mini" v-if="hasPrivilege('502020090604200029')"
                @click="_deleteRoomFee(scope.row)">
                {{ $t('ownerDetailRoomFee.cancel') }}
              </el-button>
              <el-button type="text" size="mini"
                v-if="scope.row.state != '2009001' && hasPrivilege('502021070488970005')"
                @click="_finishRoomFee(scope.row)">
                {{ $t('ownerDetailRoomFee.end') }}
              </el-button>
              <el-button type="text" size="mini"
                v-if="scope.row.state != '2009001' && hasPrivilege('502020090427190001')"
                @click="_editRoomFee(scope.row)">
                {{ $t('ownerDetailRoomFee.change') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column> -->
      </el-table>
      <el-row>
        <el-col :span="12">
          <div>{{ $t('aOwnerDetailRoomFee.note1') }}</div>
          <div>{{ $t('aOwnerDetailRoomFee.note2') }}</div>
        </el-col>
        <el-col :span="4">
          <span>{{ $t('aOwnerDetailRoomFee.arrearsSubtotal') }}</span>
          {{ ownerDetailRoomFeeInfo.totalAmount }}
        </el-col>
        <el-col :span="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" :total="pagination.total" layout="total, prev, pager, next, jumper">
          </el-pagination>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
import { listRoomsByOwner } from '@/api/owner/ownerDetailRoomFeeApi'
import {listFee} from '@/api/fee/feeApi'
import { getCommunityId } from '@/api/community/communityApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'OwnerDetailRoomFee',
  components: {
  },
  data() {
    return {
      ownerDetailRoomFeeInfo: {
        fees: [],
        payObjs: [],
        payerObjIds: [],
        ownerId: '',
        state: '2008001',
        totalAmount: 0,
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(owner) {
      this.ownerDetailRoomFeeInfo.ownerId = owner.ownerId
      this._loadDetailRoomsData()
    },
    _loadOwnerDetailRoomFeeData(page, row) {
      const _payerObjIds = this.ownerDetailRoomFeeInfo.payerObjIds.join(',')
      const params = {
        page: page,
        row: row,
        communityId: getCommunityId(),
        payerObjIds: _payerObjIds,
        state: this.ownerDetailRoomFeeInfo.state
      }

      listFee(params).then(response => {
        this.ownerDetailRoomFeeInfo.fees = response.fees
        this.pagination.total = response.total
        let _totalAmount = 0.0
        response.fees.forEach(item => {
          _totalAmount += parseFloat(item.amountOwed)
        })
        this.ownerDetailRoomFeeInfo.totalAmount = _totalAmount.toFixed(2)
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _loadDetailRoomsData() {
      const params = {
        communityId: getCommunityId(),
        ownerId: this.ownerDetailRoomFeeInfo.ownerId,
        page: 1,
        row: 100
      }

      this.ownerDetailRoomFeeInfo.payerObjIds = []
      listRoomsByOwner(params).then(response => {
        console.log('params',response)

        this.ownerDetailRoomFeeInfo.payObjs = response.rooms
        response.rooms.forEach(room => {
          this.ownerDetailRoomFeeInfo.payerObjIds.push(room.roomId)
        })
        this._loadOwnerDetailRoomFeeData(1, this.pagination.pageSize)
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _openTempImportRoomFeeModal() {
      this.$refs.roomsImportTempFee.open({
        ownerId: this.ownerDetailRoomFeeInfo.ownerId,
        ownerName: this.ownerDetailRoomFeeInfo.ownerName
      })
    },
    _payRoomFee(fee) {
      this.$router.push(`/views/fee/payFeeOrder?feeId=${fee.feeId}`)
    },
    _editRoomFee(fee) {
      this.$emit('editFee', fee)
    },
    _payRoomFeeHis(fee) {
      this.$router.push(`/views/fee/propertyFee?${this.objToGetParam(fee)}`)
    },
    _deleteRoomFee(fee) {
      this.$emit('deleteFee', {
        communityId: getCommunityId(),
        feeId: fee.feeId
      })
    },
    _finishRoomFee(fee) {
      this.$emit('finishFee', {
        communityId: getCommunityId(),
        feeId: fee.feeId
      })
    },
    _toRoomOwePayFee() {
      const _ids = this.ownerDetailRoomFeeInfo.payerObjIds
      if (!_ids || _ids.length < 1) {
        this.$message.warning(this.$t('ownerDetailRoomFee.selectRoom'))
        return
      }
      this.$router.push(`/views/fee/owePayFeeOrder?payObjId=${_ids.join(',')}&payObjType=3333&roomName=`)
    },
    _printOwnOrder() {
      const _ids = this.ownerDetailRoomFeeInfo.payerObjIds
      if (!_ids || _ids.length < 1) {
        this.$message.warning(this.$t('ownerDetailRoomFee.selectRoom'))
        return
      }
      window.open(`/#/views/fee/printOweFee?payObjId=${_ids.join(',')}&payObjType=3333&payObjName=`)
    },
    _openRoomsCreateFeeModal() {
      this.$refs.roomsCreateFee.open({
        ownerId: this.ownerDetailRoomFeeInfo.ownerId,
        ownerName: this.ownerDetailRoomFeeInfo.ownerName
      })
    },
    _getRoomAttrValue(attrs, specCd) {
      let _value = ""
      attrs.forEach(item => {
        if (item.specCd == specCd) {
          _value = item.value
          return
        }
      })
      return _value
    },
    _getRoomDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.deadlineTime)
    },
    _getRoomEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return dateFormat(fee.endTime)
    },
    _chanagePayerObjId() {
      this._loadOwnerDetailRoomFeeData(1, this.pagination.pageSize)
    },
    _changeContractConfigId() {
      this._loadOwnerDetailRoomFeeData(1, this.pagination.pageSize)
    },
    _openBatchPayRoomFeeModal() {
      this.$router.push(`/property/batchPayFeeOrder?ownerId=${this.ownerDetailRoomFeeInfo.ownerId}&payerObjType=3333`)
    },
    _openProxyFeeModal() {
      this.$refs.roomsProxyFee.open({
        ownerId: this.ownerDetailRoomFeeInfo.ownerId,
        ownerName: this.ownerDetailRoomFeeInfo.ownerName
      })
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailRoomFeeData(val, this.pagination.pageSize)
    },

    objToGetParam(obj) {
      return Object.keys(obj).map(key => `${key}=${obj[key]}`).join('&')
    }
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailRoomFeeInfo.ownerId = data.ownerId
      this._loadDetailRoomsData()
    })
  }
}
</script>
