<template>
  <el-dialog :title="$t('searchRoom.title')" :visible.sync="dialogVisible" width="70%" @close="handleClose">
    <div class="ibox">
      <el-row v-if="showSearchCondition">
        <el-col :span="4">
          <el-input :placeholder="$t('searchRoom.floorNumPlaceholder')" :readonly="floorNumInputReadonly"
            v-model.trim="currentFloorNum" clearable />
        </el-col>
        <el-col :span="4" :offset="1">
          <el-input :placeholder="$t('searchRoom.roomNumPlaceholder')" v-model.trim="currentRoomNum" clearable />
        </el-col>
        <el-col :span="6" :offset="1">
          <el-button type="primary" @click="searchRooms">
            <i class="el-icon-search"></i>
            {{ $t('searchRoom.search') }}
          </el-button>
          <el-button type="primary" @click="resetRooms">
            <i class="el-icon-refresh"></i>
            {{ $t('searchRoom.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table :data="rooms" style="width: 100%; margin-top: 15px" border stripe>
        <el-table-column prop="roomId" :label="$t('searchRoom.roomId')" align="center" />
        <el-table-column prop="floorNum" :label="$t('searchRoom.floorNum')" align="center">
          <template #default="{ row }">
            {{ row.floorNum }}{{ $t('searchRoom.building') }}
          </template>
        </el-table-column>
        <el-table-column prop="unitNum" :label="$t('searchRoom.unitNum')" align="center">
          <template #default="{ row }">
            {{ row.unitNum }}{{ $t('searchRoom.unit') }}
          </template>
        </el-table-column>
        <el-table-column prop="roomNum" :label="$t('searchRoom.roomNum')" align="center">
          <template #default="{ row }">
            {{ row.roomNum }}{{ $t('searchRoom.room') }}
          </template>
        </el-table-column>
        <el-table-column prop="layer" :label="$t('searchRoom.layer')" align="center">
          <template #default="{ row }">
            {{ row.layer }}{{ $t('searchRoom.floor') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('searchRoom.operation')" align="center" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="mini" @click="chooseRoom(row)">
              {{ $t('searchRoom.choose') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="pagination.currentPage" :page-sizes="[10, 15, 20, 30]" :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 15px" />
    </div>
  </el-dialog>
</template>

<script>
import { queryRooms, queryRoomsWithSell, queryRoomsWithOutSell } from '@/api/room/roomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SearchRoom',
  props: {
    roomFlag: {
      type: String,
      default: ''
    },
    showSearchCondition: {
      type: String,
      default: 'true'
    }
  },
  data() {
    return {
      dialogVisible: false,
      rooms: [],
      currentRoomNum: '',
      currentFloorNum: '',
      floorNumInputReadonly: false,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  computed: {
    isShowSearchCondition() {
      return this.showSearchCondition === 'true'
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.refreshSearchRoomData()
      this.loadAllRoomInfo()
    },
    handleClose() {
      this.dialogVisible = false
    },
    async loadAllRoomInfo() {
      try {
        const params = {
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          communityId: getCommunityId(),
          roomNum: this.currentRoomNum,
          floorNum: this.currentFloorNum,
          roomFlag: this.roomFlag
        }

        let response
        if (this.roomFlag == '1') {
          response = await queryRoomsWithSell(params)
        } else if (this.roomFlag == '2') {
          response = await queryRoomsWithOutSell(params)
        } else {
          response = await queryRooms(params)
        }

        this.rooms = response.rooms || []
        this.pagination.total = response.total || 0
      } catch (error) {
        console.error('Failed to load room info:', error)
        this.$message.error(error)
      }
    },
    chooseRoom(room) {
      this.$emit('chooseRoom', room)
      this.$emit('load-data', { roomId: room.roomId })
      this.dialogVisible = false
    },
    searchRooms() {
      this.pagination.currentPage = 1
      this.loadAllRoomInfo()
    },
    resetRooms() {
      this.currentFloorNum = ''
      this.currentRoomNum = ''
      this.pagination.currentPage = 1
      this.loadAllRoomInfo()
    },
    refreshSearchRoomData() {
      this.currentRoomNum = ''
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadAllRoomInfo()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadAllRoomInfo()
    },
    listenerFloorInfo(floorInfo) {
      this.currentFloorNum = floorInfo.floorNum
      this.floorNumInputReadonly = true
      this.searchRooms()
    },
    showOwnerRooms(rooms) {
      this.dialogVisible = true
      this.rooms = rooms
    }
  },
}
</script>

<style scoped>
.el-input {
  width: 100%;
}
</style>