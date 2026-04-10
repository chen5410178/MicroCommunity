<template>
  <div>
    <div class="margin-top">
      <el-table :data="feeDetailRoomInfo.rooms" style="width: 100%">
        <el-table-column :label="$t('feeDetailRoom.roomId')" align="center">
          <template slot-scope="scope">{{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}</template>
        </el-table-column>
        <el-table-column prop="layer" :label="$t('feeDetailRoom.floor')" align="center"></el-table-column>
        <el-table-column prop="roomSubTypeName" :label="$t('feeDetailRoom.type')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailRoom.area')" align="center">
          <template slot-scope="scope">{{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}</template>
        </el-table-column>
        <el-table-column prop="roomRent" :label="$t('feeDetailRoom.rent')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('feeDetailRoom.roomStatus')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailRoom.roomArrears')" align="center">
          <template slot-scope="scope">{{ scope.row.roomOweFee ||
            '0.00'}}({{ $t('feeDetailRoom.updateDaily') }})</template>
        </el-table-column>
        <el-table-column :label="$t('feeDetailRoom.operation')" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state != '2002'" size="mini" @click="_toSimplifyAcceptance(scope.row)">
              {{ $t('feeDetailRoom.businessAcceptance') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next" :total="pagination.total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryRooms } from '@/api/fee/feeDetailRoomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailRoom',
  data() {
    return {
      feeDetailRoomInfo: {
        rooms: [],
        roomId: '',
        roomNum: '',
        allOweFeeAmount: '0'
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.feeDetailRoomInfo.roomId = params.payerObjId
      this._loadFeeDetailRoomData()
    },
    _loadFeeDetailRoomData() {
      const params = {
        communityId: getCommunityId(),
        roomId: this.feeDetailRoomInfo.roomId,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      queryRooms(params).then(res => {
        this.feeDetailRoomInfo.rooms = res.rooms
        this.pagination.total = res.total
      }).catch(error => {
        console.error('Failed to load room details:', error)
      })
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadFeeDetailRoomData()
    },
    _toSimplifyAcceptance(room) {

      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${room.floorNum}-${room.unitNum}-${room.roomNum}`)

    }
  }
}
</script>