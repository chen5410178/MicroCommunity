<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="22" class="text-right"></el-col>
      <el-col :span="2" class="text-right">
       
      </el-col>
    </el-row>
    <div>
      <el-table
        :data="simplifyOwnerRoomsInfo.rooms"
        style="margin-top:10px"
        border
        stripe
      >
        <el-table-column :label="$t('simplifyOwnerRooms.roomCode')" align="center">
          <template slot-scope="scope">
            {{scope.row.floorNum}}-{{scope.row.unitNum}}-{{scope.row.roomNum}}
          </template>
        </el-table-column>
        <el-table-column prop="layer" :label="$t('simplifyOwnerRooms.floor')" align="center"></el-table-column>
        <el-table-column prop="roomSubTypeName" :label="$t('simplifyOwnerRooms.type')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyOwnerRooms.area')" align="center">
          <template slot-scope="scope">
            {{scope.row.builtUpArea}}/{{scope.row.roomArea}}
          </template>
        </el-table-column>
        <el-table-column prop="roomRent" :label="$t('simplifyOwnerRooms.rent')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyOwnerRooms.validity')" align="center">
          <template slot-scope="scope">
            {{scope.row.startTime}}<br>~{{scope.row.endTime}}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('simplifyOwnerRooms.roomStatus')" align="center"></el-table-column>
        <el-table-column :label="$t('simplifyOwnerRooms.roomOweFee')" align="center">
          <template slot-scope="scope">
            {{scope.row.roomOweFee || '0.00'}}({{$t('simplifyOwnerRooms.updateDaily')}})
          </template>
        </el-table-column>
        <el-table-column :label="$t('simplifyOwnerRooms.actions')" align="center">
          <template slot-scope="scope">
            <el-button-group v-if="simplifyOwnerRoomsInfo.roomId != scope.row.roomId">
              <el-button size="mini" @click="_toChooseOwnerRoomModel(scope.row)">
                {{ $t('simplifyOwnerRooms.choose') }}
              </el-button>
            </el-button-group>
            <div v-else>
              {{ $t('simplifyOwnerRooms.currentRoom') }}
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-row>
        <el-col :span="16"></el-col>
        <el-col :span="8" class="float-right">
          <span>{{ $t('simplifyOwnerRooms.subtotalOweFee') }}：{{simplifyOwnerRoomsInfo.allOweFeeAmount}}</span>
        </el-col>
      </el-row>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryRoomsByOwner } from '@/api/fee/simplifyOwnerRoomsApi'

export default {
  name: 'SimplifyOwnerRooms',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      simplifyOwnerRoomsInfo: {
        rooms: [],
        ownerId: '',
        roomId: '',
        total: 0,
        records: 1,
        allOweFeeAmount: 0
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this._initEvent()
  },
  methods: {
    _initEvent() {
    },
    handleSwitch(param) {
      if (!param.ownerId) return
      this.clearSimplifyOwnerRoomsInfo()
      Object.assign(this.simplifyOwnerRoomsInfo, param)
      this._listSimplifyOwnerRooms(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this._listSimplifyOwnerRooms(val, this.DEFAULT_ROWS)
    },
    async _listSimplifyOwnerRooms() {
      try {
        const res = await queryRoomsByOwner({
          ownerId: this.simplifyOwnerRoomsInfo.ownerId,
          communityId: getCommunityId()
        })
        this.simplifyOwnerRoomsInfo.rooms = res.rooms
        this._computeOwnerRoomOweFeeAmount()
        this.simplifyOwnerRoomsInfo.total = res.total
        this.simplifyOwnerRoomsInfo.records = res.records
        this.total = res.total
      } catch (error) {
        console.error('Request failed:', error)
      }
    },
    _toChooseOwnerRoomModel(room) {
      this.$emit('notifyRoom', room)
    },
    _computeOwnerRoomOweFeeAmount() {
      let total = 0
      this.simplifyOwnerRoomsInfo.allOweFeeAmount = 0
      if (!this.simplifyOwnerRoomsInfo.rooms || this.simplifyOwnerRoomsInfo.rooms.length < 1) return
      
      this.simplifyOwnerRoomsInfo.rooms.forEach(room => {
        if (room.roomOweFee) {
          total += parseFloat(room.roomOweFee)
        }
      })
      this.simplifyOwnerRoomsInfo.allOweFeeAmount = total.toFixed(2)
    },
    clearSimplifyOwnerRoomsInfo() {
      this.simplifyOwnerRoomsInfo = {
        rooms: [],
        ownerId: '',
        roomId: '',
        total: 0,
        records: 1,
        allOweFeeAmount: 0
      }
    },
    _openSimplifyOwnerRoomsBatchPayFeeModal() {
      this.$router.push(`/property/batchPayFeeOrder?ownerId=${this.simplifyOwnerRoomsInfo.ownerId}&payerObjType=3333`)
    },
    hasPrivilege(code) {
      void code; // 占位，消除未使用警告
      return true;
    },
    open(params) {
      this.handleSwitch(params)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.float-right {
  float: right;
}
.text-right {
  text-align: right;
}
</style>