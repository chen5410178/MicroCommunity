<template>
  <div class="">
    <div class="flex justify-end">
      <div :span="2" class="padding-right-xs padding-left-xl"></div>
      <div :span="2" class="padding-right-xs padding-right-xl"></div>
      <div :span="8" class="text-right">
        <!-- <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502023021978930012')"
          @click="_openAddContractRoom">
          <i class="el-icon-plus"></i>
          {{ $t('contractDetailRoom.addPropertyChange') }}
        </el-button> -->
      </div>
    </div>
    <div class="margin-top">
      <el-table :data="contractDetailRoomInfo.rooms" style="width: 100%" border stripe>
        <el-table-column prop="roomName" :label="$t('contractDetailRoom.room')" align="center"></el-table-column>
        <el-table-column prop="layer" :label="$t('contractDetailRoom.floor')" align="center"></el-table-column>
        <el-table-column prop="roomSubTypeName" :label="$t('contractDetailRoom.type')" align="center"></el-table-column>
        <el-table-column :label="$t('contractDetailRoom.area')" align="center">
          <template slot-scope="scope">
            {{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}
          </template>
        </el-table-column>
        <el-table-column prop="roomRent" :label="$t('contractDetailRoom.rent')" align="center"></el-table-column>
        <el-table-column prop="stateName" :label="$t('contractDetailRoom.roomStatus')" align="center"></el-table-column>
        <el-table-column :label="$t('contractDetailRoom.operation')" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state != '2002'" size="mini" @click="_toSimplifyAcceptance(scope.row)">
              {{ $t('contractDetailRoom.businessAcceptance') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="margin-top">
        <el-col :span="4">
          <span>{{ $t('contractDetailRoom.contractArea') }}:</span>
          {{ contractDetailRoomInfo.totalArea }}
        </el-col>
        <el-col :span="8" class="text-right">
          <el-pagination @current-change="handlePageChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next"
            :total="pagination.total"></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryContractRoom } from '@/api/contract/contractDetailRoomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailRoom',
  data() {
    return {
      contractDetailRoomInfo: {
        rooms: [],
        contractId: '',
        roomNum: '',
        totalArea: '0'
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(data) {
      this.contractDetailRoomInfo.contractId = data.contractId
      this._loadContractDetailRoomData(1, this.pagination.pageSize)
    },
    _loadContractDetailRoomData(page, row) {
      const params = {
        contractId: this.contractDetailRoomInfo.contractId,
        page: page || 1,
        row: row || 10
      }

      queryContractRoom(params).then(response => {
        this.contractDetailRoomInfo.rooms = response.data
        this._sumRoomArea()
        this.pagination.total = response.total || 0
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _sumRoomArea() {
      const rooms = this.contractDetailRoomInfo.rooms
      if (!rooms || rooms.length < 1) {
        this.contractDetailRoomInfo.totalArea = '0'
        return '0'
      }
      let totalArea = 0
      rooms.forEach(room => {
        totalArea += parseFloat(room.builtUpArea)
      })
      this.contractDetailRoomInfo.totalArea = totalArea.toFixed(2)
      return totalArea.toFixed(2)
    },
    _toSimplifyAcceptance(room) {
        this.$router.push('/pages/property/simplifyAcceptance?tab=业务受理&searchType=1&searchValue=' + `${room.floorNum}-${room.unitNum}-${room.roomNum}`)

    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this._loadContractDetailRoomData(currentPage, this.pagination.pageSize)
    },
  },
  created() {
    this.communityId = getCommunityId()
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