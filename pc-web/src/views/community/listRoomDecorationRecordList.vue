<template>
  <div class="list-room-decoration-record-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
          <div>
            <span>{{ roomDecorationRecordsInfo.conditions.roomName }}</span>
            <span>{{ $t('listRoomDecorationRecord.title') }}</span>
          </div>
          <div >
            <el-button type="primary" size="small" @click="_openAddModal(roomDecorationRecordsInfo.roomRenovation)">
              <i class="el-icon-plus"></i>
              {{ $t('listRoomDecorationRecord.add') }}
            </el-button>
            <el-button type="primary" size="small" style="margin-left: 10px" @click="_goBack()">
              <i class="el-icon-close"></i>
              {{ $t('listRoomDecorationRecord.back') }}
            </el-button>
          </div>

      </div>

      <el-table :data="roomDecorationRecordsInfo.roomRenovationRecords" border style="width: 100%">
        <el-table-column prop="recordId" :label="$t('listRoomDecorationRecord.recordId')" align="center" />
        <el-table-column prop="roomName" :label="$t('listRoomDecorationRecord.room')" align="center" />
        <el-table-column prop="staffName" :label="$t('listRoomDecorationRecord.operator')" align="center" />
        <el-table-column prop="createTime" :label="$t('listRoomDecorationRecord.createTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('listRoomDecorationRecord.status')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.state === '1000'">
              {{ scope.row.stateName }}({{ $t('listRoomDecorationRecord.originalImage') }})
            </span>
            <span v-else>{{ scope.row.stateName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isTrueName" :label="$t('listRoomDecorationRecord.isViolation')" align="center" />
        <el-table-column prop="remark" :label="$t('listRoomDecorationRecord.remark')" align="center" />
        <el-table-column :label="$t('listRoomDecorationRecord.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openRoomRenovationRecordDetailsModel(scope.row)">
              {{ $t('listRoomDecorationRecord.viewDetails') }}
            </el-button>
            <el-button size="mini" type="danger" @click="_openDeleteRoomRenovationRecordModel(scope.row)">
              {{ $t('listRoomDecorationRecord.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <room-decoration-record ref="roomDecorationRecord" @success="handleSuccess" />
    <delete-room-decoration-record ref="deleteRoomDecorationRecord" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryRoomRenovationRecord } from '@/api/community/listRoomDecorationRecordApi'
import { getCommunityId } from '@/api/community/communityApi'
import RoomDecorationRecord from '@/components/community/RoomDecorationRecord'
import DeleteRoomDecorationRecord from '@/components/community/DeleteRoomDecorationRecord'

export default {
  name: 'ListRoomDecorationRecord',
  components: {
    RoomDecorationRecord,
    DeleteRoomDecorationRecord
  },
  data() {
    return {
      roomDecorationRecordsInfo: {
        roomRenovationRecords: [],
        roomRenovation: [],
        conditions: {
          rId: '',
          roomName: '',
          roomId: '',
          stateName: '',
          state: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.roomDecorationRecordsInfo.conditions.communityId = this.communityId
    this.roomDecorationRecordsInfo.conditions.rId = this.$route.query.rId
    this.roomDecorationRecordsInfo.conditions.roomId = this.$route.query.roomId
    this.roomDecorationRecordsInfo.conditions.roomName = this.$route.query.roomName
    this.roomDecorationRecordsInfo.conditions.state = this.$route.query.state
    this.roomDecorationRecordsInfo.conditions.stateName = this.$route.query.stateName
    this._listRoomRenovationRecords(this.page.current, this.page.size)
  },
  methods: {
    async _listRoomRenovationRecords(page, size) {
      try {
        const params = {
          page,
          row: size,
          ...this.roomDecorationRecordsInfo.conditions
        }
        const { data, total } = await queryRoomRenovationRecord(params)
        this.roomDecorationRecordsInfo.roomRenovationRecords = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('listRoomDecorationRecord.fetchError'))
      }
    },
    _openAddModal(roomRenovation) {
      roomRenovation.push(this.roomDecorationRecordsInfo.conditions.rId)
      roomRenovation.push(this.roomDecorationRecordsInfo.conditions.roomId)
      roomRenovation.push(this.roomDecorationRecordsInfo.conditions.roomName)
      roomRenovation.push(this.roomDecorationRecordsInfo.conditions.state)
      roomRenovation.push(this.roomDecorationRecordsInfo.conditions.stateName)
      this.$refs.roomDecorationRecord.open(roomRenovation)
    },
    _openDeleteRoomRenovationRecordModel(record) {
      this.$refs.deleteRoomDecorationRecord.open(record)
    },
    _openRoomRenovationRecordDetailsModel(record) {
      this.$router.push({
        path: '/views/community/listRoomRenovationRecordDetails',
        query: {
          recordId: record.recordId,
          roomName: record.roomName,
          state: record.state
        }
      })
    },
    handleSuccess() {
      this._listRoomRenovationRecords(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listRoomRenovationRecords(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listRoomRenovationRecords(val, this.page.size)
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.list-room-decoration-record-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;

      span {
        font-size: 16px;
        font-weight: bold;
      }
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>