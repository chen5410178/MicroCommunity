<template>
  <el-dialog :title="$t('ownerRooms.title')" :visible.sync="visible" width="70%" :before-close="handleClose">
    <el-table :data="ownerRoomsInfo.rooms" style="width: 100%">
      <el-table-column prop="roomId" :label="$t('ownerRooms.roomId')" align="center" />
      <el-table-column prop="layer" :label="$t('ownerRooms.layer')" align="center" />
      <el-table-column prop="roomSubTypeName" :label="$t('ownerRooms.type')" align="center" />
      <el-table-column :label="$t('ownerRooms.area')" align="center">
        <template slot-scope="scope">
          {{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}
        </template>
      </el-table-column>
      <el-table-column prop="roomRent" :label="$t('ownerRooms.rent')" align="center" />
      <el-table-column prop="stateName" :label="$t('ownerRooms.status')" align="center" />
      <el-table-column :label="$t('ownerRooms.oweFee')" align="center">
        <template slot-scope="scope">
          {{ scope.row.roomOweFee || '0.00' }} ({{ $t('ownerRooms.updateDaily') }})
        </template>
      </el-table-column>
    </el-table>

    <el-row class="mt-3 margin-top">
      <el-col :span="8">
        <span>{{ $t('ownerRooms.subtotal') }}: {{ ownerRoomsInfo.allOweFeeAmount }}</span>
      </el-col>
      <el-col :span="16" class="text-right">
        <el-pagination :current-page="pagination.currentPage" :page-size="pagination.pageSize" :total="pagination.total"
          layout="prev, pager, next" @current-change="handlePageChange" />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { queryRoomsByOwner } from '@/api/room/ownerRoomsApi'

export default {
  name: 'OwnerRooms',
  data() {
    return {
      visible: false,
      ownerRoomsInfo: {
        rooms: [],
        ownerId: '',
        allOweFeeAmount: 0
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
      this.ownerRoomsInfo.ownerId = params.ownerId
      this.visible = true
      this.loadOwnerRoomInfo()
    },
    handleClose() {
      this.visible = false
    },
    handlePageChange(page) {
      this.pagination.currentPage = page
      this.loadOwnerRoomInfo()
    },
    async loadOwnerRoomInfo() {
      try {
        const params = {
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          communityId: this.getCommunityId(),
          ownerId: this.ownerRoomsInfo.ownerId
        }

        const response = await queryRoomsByOwner(params)
        this.ownerRoomsInfo.rooms = response.rooms
        this.pagination.total = response.total || 0
        this.computeOwnerRoomOweFeeAmount()
      } catch (error) {
        console.error('加载业主房屋信息失败:', error)
      }
    },
    computeOwnerRoomOweFeeAmount() {
      let totalOweFeeAmount = 0
      this.ownerRoomsInfo.rooms.forEach(room => {
        if (room.roomOweFee) {
          totalOweFeeAmount += parseFloat(room.roomOweFee)
        }
      })
      this.ownerRoomsInfo.allOweFeeAmount = totalOweFeeAmount.toFixed(2)
    }
  }
}
</script>