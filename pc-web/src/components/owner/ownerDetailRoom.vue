<template>
  <div class="margin-top">
    <el-row class="">
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-input v-model="ownerDetailRoomInfo.roomNum" :placeholder="$t('ownerDetailRoom.pleaseEnterRoomNumber')" />
      </el-col>
      <el-col :span="4" class="padding-right-xs padding-right-xl">
        <el-button type="primary" size="small" @click="_qureyOwnerDetailRoom">
          <i class="el-icon-search"></i>{{ $t('common.query') }}
        </el-button>
        <el-button type="primary" size="small" @click="_resetOwnerDetailRoom">
          <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
        </el-button>
      </el-col>
      <el-col :span="16" class="text-right">
        <el-button v-if="hasPrivilege('502023021978930012')" type="primary" size="small" @click="_openAddOwnerRoom">
          <i class="el-icon-plus"></i>
          {{ $t('ownerDetailRoom.deliverRoom') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="ownerDetailRoomInfo.rooms" border style="width: 100%">
        <el-table-column prop="roomNum" :label="$t('ownerDetailRoom.roomNumber')" align="center">
          <template slot-scope="scope">
            {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
          </template>
        </el-table-column>
        <el-table-column prop="layer" :label="$t('ownerDetailRoom.floor')" align="center" />
        <el-table-column prop="roomSubTypeName" :label="$t('ownerDetailRoom.type')" align="center" />
        <el-table-column :label="$t('ownerDetailRoom.area')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}
          </template>
        </el-table-column>
        <el-table-column prop="roomRent" :label="$t('ownerDetailRoom.rent')" align="center" />
        <el-table-column :label="$t('ownerDetailRoom.validity')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('ownerDetailRoom.roomStatus')" align="center" />
        <el-table-column :label="$t('ownerDetailRoom.roomArrears')" align="center">
          <template slot-scope="scope">
            {{ scope.row.roomOweFee || '0.00' }}({{ $t('ownerDetailRoom.updateDaily') }})
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="250">
          <template slot-scope="scope">
            <el-button v-if="hasPrivilege('502020082493857941')" size="mini" @click="_openEditRoomModel(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button v-if="hasPrivilege('502023021973760015')" size="mini" @click="ownerExitRoomModel(scope.row)">
              {{ $t('ownerDetailRoom.checkOut') }}
            </el-button>
            <el-button v-if="scope.row.state != '2002'" size="mini" @click="_toSimplifyAcceptance(scope.row)">
              {{ $t('ownerDetailRoom.businessAcceptance') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="8">
          <span>
            {{ $t('ownerDetailRoom.arrearsSubtotal') }}:
            {{ ownerDetailRoomInfo.allOweFeeAmount }}
          </span>
        </el-col>
        <el-col :span="16" class="text-right">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" :total="pagination.total" layout="total, prev, pager, next, jumper" />
        </el-col>
      </el-row>
    </div>

    <edit-room ref="editRoom" @refresh="_loadOwnerDetailRoomData" />
    <owner-exit-room ref="ownerExitRoom" @success="_loadOwnerDetailRoomData" />
  </div>
</template>

<script>
import EditRoom from '@/components/room/editRoom'
import { queryRoomsByOwner } from '@/api/owner/ownerDetailRoomApi'

import OwnerExitRoom from '@/components/owner/ownerExitRoom'
import { getCommunityId } from '@/api/community/communityApi'


export default {
  name: 'OwnerDetailRoom',
  components: {
    EditRoom,
    OwnerExitRoom
  },
  data() {
    return {
      ownerDetailRoomInfo: {
        rooms: [],
        ownerId: '',
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
    open(ownerId, ownerName, ownerLink) {
      this.ownerDetailRoomInfo.ownerId = ownerId
      this.ownerDetailRoomInfo.ownerName = ownerName
      this.ownerDetailRoomInfo.ownerLink = ownerLink
      this._loadOwnerDetailRoomData()
    },
    async _loadOwnerDetailRoomData() {
      try {
        const params = {
          communityId: getCommunityId(),
          ownerId: this.ownerDetailRoomInfo.ownerId,
          roomNum: this.ownerDetailRoomInfo.roomNum,
          page: this.pagination.currentPage,
          row: this.pagination.pageSize
        }

        const response = await queryRoomsByOwner(params)
        this.ownerDetailRoomInfo.rooms = response.rooms
        this.pagination.total = response.total
        this._computeOwnerRoomOweFeeAmount()
      } catch (error) {
        console.error('Failed to load owner room data:', error)
      }
    },
    _qureyOwnerDetailRoom() {
      this.pagination.currentPage = 1
      this._loadOwnerDetailRoomData()
    },
    _resetOwnerDetailRoom() {
      this.ownerDetailRoomInfo.roomNum = ''
      this._qureyOwnerDetailRoom()
    },
    _computeOwnerRoomOweFeeAmount() {
      const rooms = this.ownerDetailRoomInfo.rooms
      let totalOweFeeAmount = 0
      this.ownerDetailRoomInfo.allOweFeeAmount = 0

      if (!rooms || rooms.length < 1) return

      rooms.forEach(room => {
        if (room.roomOweFee) {
          totalOweFeeAmount += parseFloat(room.roomOweFee)
        }
      })

      this.ownerDetailRoomInfo.allOweFeeAmount = totalOweFeeAmount.toFixed(2)
    },
    _openAddOwnerRoom() {
      console.log(this.ownerDetailRoomInfo.ownerId)
      this.$router.push({
        path: '/views/owner/ownerBindRoom',
        query: { ownerId: this.ownerDetailRoomInfo.ownerId }
      })
    },
    ownerExitRoomModel(room) {
      this.$refs.ownerExitRoom.open({
        ownerId: this.ownerDetailRoomInfo.ownerId,
        roomId: room.roomId
      })
    },
    _toSimplifyAcceptance(room) {
      this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${room.floorNum}-${room.unitNum}-${room.roomNum}`)

    },
    _openEditRoomModel(room) {
      this.$refs.editRoom.open(room)
    },
    handleCurrentChange(currentPage) {
      this.pagination.currentPage = currentPage
      this._loadOwnerDetailRoomData()
    },
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

.padding-right-xs {
  padding-right: 5px;
}

.padding-left-xl {
  padding-left: 20px;
}

.text-right {
  text-align: right;
}
</style>