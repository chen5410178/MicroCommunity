<template>
  <div class="room-detail-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-input v-model="roomNum" :placeholder="$t('adminRoomFee.roomNumPlaceholder')" clearable />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="loadData">
          {{ $t('common.search') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="rooms" class="margin-top" border>
      <el-table-column :label="$t('adminRoomFee.roomNum')" align="center">
        <template slot-scope="scope">
          {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
        </template>
      </el-table-column>
      <el-table-column prop="layer" :label="$t('adminRoomFee.floor')" align="center" />
      <el-table-column prop="roomSubTypeName" :label="$t('adminRoomFee.type')" align="center" />
      <el-table-column :label="$t('adminRoomFee.area')" align="center">
        <template slot-scope="scope">
          {{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}
        </template>
      </el-table-column>
      <el-table-column prop="roomRent" :label="$t('adminRoomFee.rent')" align="center" />
      <el-table-column :label="$t('adminRoomFee.validity')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}<br>
          ~{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('adminRoomFee.roomStatus')" align="center" />
      <el-table-column prop="roomOweFee" :label="$t('adminRoomFee.roomOweFee')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button size="mini" @click="toDetail(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :current-page="page.current" :page-size="page.size" :total="page.total"
      layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { queryAdminOwnerRooms } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'ARoomDetailRoom',
  data() {
    return {
      rooms: [],
      roomNum: '',
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(data) {
      this.ownerId = data.ownerId
      this.roomId = data.roomId
      this.loadData()
    },
    async loadData() {
      const params = {
        ownerId: this.ownerId,
        roomNum: this.roomNum,
        roomId: this.roomId,
        page: this.page.current,
        row: this.page.size
      }

      const res = await queryAdminOwnerRooms(params)
      this.rooms = res.rooms
      this.page.total = res.total

    },
    toDetail(room) {
      window.open(`/#/pages/community/adminRoomDetail?roomId=${room.roomId}`)
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadData()
    }
  }
}
</script>