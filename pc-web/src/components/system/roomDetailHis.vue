<template>
  <div class="room-detail-his">
    <el-table :data="roomDetailHisInfo.rooms" border style="width: 100%" v-loading="loading">
      <el-table-column :label="$t('roomDetailHis.operate')" align="center">
        <template slot-scope="scope">
          {{ _getRoomHisOperate(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.userName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.userName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.createTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.roomNum')" align="center">
        <template slot-scope="scope">
          {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.layer')" align="center">
        <template slot-scope="scope">
          {{ scope.row.layer }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.roomSubTypeName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.roomSubTypeName }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.area')" align="center">
        <template slot-scope="scope">
          {{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.roomRent')" align="center">
        <template slot-scope="scope">
          {{ scope.row.roomRent }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('roomDetailHis.stateName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.stateName }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { queryHisRoom } from '@/api/system/operateDataLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RoomDetailHis',
  data() {
    return {
      loading: false,
      roomDetailHisInfo: {
        rooms: [],
        roomId: '',
        roomName: '',
        logStartTime: '',
        logEndTime: '',
        staffNameLike: '',
        payerObjName: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(conditions) {
      this.roomDetailHisInfo = {
        ...this.roomDetailHisInfo,
        ...conditions
      }
      this._loadRoomDetailHisData()
    },
    async _loadRoomDetailHisData() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          roomId: this.roomDetailHisInfo.roomId,
          roomName: this.roomDetailHisInfo.roomName,
          logStartTime: this.roomDetailHisInfo.logStartTime,
          logEndTime: this.roomDetailHisInfo.logEndTime,
          staffNameLike: this.roomDetailHisInfo.staffNameLike,
          payerObjName: this.roomDetailHisInfo.payerObjName,
          communityId: getCommunityId() 
        }

        const { data, total } = await queryHisRoom(params)
        this.roomDetailHisInfo.rooms = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load room history:', error)
      } finally {
        this.loading = false
      }
    },
    _getRoomHisOperate(room) {
      const roomCount = this.roomDetailHisInfo.rooms.filter(item => item.bId === room.bId).length

      if (roomCount <= 1) {
        if (room.operate === 'ADD') return this.$t('roomDetailHis.add')
        if (room.operate === 'DEL') return this.$t('roomDetailHis.delete')
        return '-'
      }

      if (room.operate === 'ADD') return this.$t('roomDetailHis.modifyNew')
      if (room.operate === 'DEL') return this.$t('roomDetailHis.modifyOld')
      return '-'
    },
    handleSizeChange(size) {
      this.page.size = size
      this._loadRoomDetailHisData()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this._loadRoomDetailHisData()
    }
  }
}
</script>

<style scoped>
.room-detail-his {
  padding: 20px;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>