<template>
  <div class="list-apply-room-discount-record-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <div class="card-header">
          <div>
            <span>{{ listApplyRoomDiscountRecordsInfo.conditions.roomName }}</span>
            <span class="header-title">{{ $t('listApplyRoomDiscountRecord.title') }}</span>
          </div>
          <div class="header-tools">
            <el-button type="primary" size="small" @click="_openAddModal(listApplyRoomDiscountRecordsInfo.applyRoomDiscount)">
              <i class="el-icon-plus"></i>
              {{ $t('common.add') }}
            </el-button>
            <el-button type="primary" size="small" style="margin-left:10px" @click="_goBack()">
              <i class="el-icon-close"></i>
              {{ $t('common.back') }}
            </el-button>
          </div>
        </div>
      </div>

      <el-table
        :data="listApplyRoomDiscountRecordsInfo.listApplyRoomDiscountRecords"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="ardrId" :label="$t('listApplyRoomDiscountRecord.id')" align="center" />
        <el-table-column prop="roomName" :label="$t('listApplyRoomDiscountRecord.room')" align="center" />
        <el-table-column prop="createUserName" :label="$t('listApplyRoomDiscountRecord.operator')" align="center" />
        <el-table-column prop="createTime" :label="$t('listApplyRoomDiscountRecord.createTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('listApplyRoomDiscountRecord.status')" align="center" />
        <el-table-column prop="isTrueName" :label="$t('listApplyRoomDiscountRecord.isViolation')" align="center" />
        <el-table-column prop="remark" :label="$t('listApplyRoomDiscountRecord.remark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="_openApplyRoomDiscountRecordDetailsModel(scope.row)">
                {{ $t('common.viewDetail') }}
              </el-button>
              <el-button size="mini" type="danger" @click="_openDeleteApplyRoomDiscountRecordModel(scope.row)">
                {{ $t('common.delete') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="listApplyRoomDiscountRecordsInfo.total"
        class="pagination"
      >
      </el-pagination>
    </el-card>

    <apply-room-discount-record ref="applyRoomDiscountRecord" @success="handleSuccess" />
    <delete-apply-room-discount-record ref="deleteApplyRoomDiscountRecord" @success="handleSuccess" />
  </div>
</template>

<script>
import { queryApplyRoomDiscountRecord } from '@/api/fee/listApplyRoomDiscountRecordApi'
import ApplyRoomDiscountRecord from '@/components/fee/applyRoomDiscountRecord'
import DeleteApplyRoomDiscountRecord from '@/components/fee/deleteApplyRoomDiscountRecord'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ListApplyRoomDiscountRecord',
  components: {
    ApplyRoomDiscountRecord,
    DeleteApplyRoomDiscountRecord
  },
  data() {
    return {
      loading: false,
      listApplyRoomDiscountRecordsInfo: {
        listApplyRoomDiscountRecords: [],
        applyRoomDiscount: [],
        total: 0,
        records: 1,
        conditions: {
          ardId: '',
          roomId: '',
          roomName: '',
          stateName: '',
          state: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10
      }
    }
  },
  created() {
    this.listApplyRoomDiscountRecordsInfo.conditions.communityId = getCommunityId()
    this.listApplyRoomDiscountRecordsInfo.conditions.ardId = this.$route.query.ardId
    this.listApplyRoomDiscountRecordsInfo.conditions.state = this.$route.query.state
    this.listApplyRoomDiscountRecordsInfo.conditions.stateName = this.$route.query.stateName
    this.listApplyRoomDiscountRecordsInfo.conditions.roomId = this.$route.query.roomId
    this.listApplyRoomDiscountRecordsInfo.conditions.roomName = this.$route.query.roomName
    this._listApplyRoomDiscountRecords(this.page.current, this.page.size)
  },
  methods: {
    async _listApplyRoomDiscountRecords(page, size) {
      try {
        this.loading = true
        const params = {
          page: page,
          row: size,
          ...this.listApplyRoomDiscountRecordsInfo.conditions
        }
        const { data, total } = await queryApplyRoomDiscountRecord(params)
        this.listApplyRoomDiscountRecordsInfo.listApplyRoomDiscountRecords = data
        this.listApplyRoomDiscountRecordsInfo.total = total
      } catch (error) {
        this.$message.error(this.$t('listApplyRoomDiscountRecord.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openAddModal(applyRoomDiscount) {
      applyRoomDiscount.push(this.listApplyRoomDiscountRecordsInfo.conditions.ardId)
      applyRoomDiscount.push(this.listApplyRoomDiscountRecordsInfo.conditions.state)
      applyRoomDiscount.push(this.listApplyRoomDiscountRecordsInfo.conditions.stateName)
      applyRoomDiscount.push(this.listApplyRoomDiscountRecordsInfo.conditions.roomId)
      applyRoomDiscount.push(this.listApplyRoomDiscountRecordsInfo.conditions.roomName)
      this.$refs.applyRoomDiscountRecord.open(applyRoomDiscount)
    },
    _openDeleteApplyRoomDiscountRecordModel(applyRoomDiscountRecord) {
      this.$refs.deleteApplyRoomDiscountRecord.open(applyRoomDiscountRecord)
    },
    _openApplyRoomDiscountRecordDetailsModel(applyRoomDiscountRecord) {
      this.$router.push({
        path: '/views/fee/listApplyRoomDiscountRecordDetails',
        query: {
          ardrId: applyRoomDiscountRecord.ardrId,
          roomName: applyRoomDiscountRecord.roomName,
          state: applyRoomDiscountRecord.state
        }
      })
    },
    handleSuccess() {
      this._listApplyRoomDiscountRecords(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listApplyRoomDiscountRecords(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listApplyRoomDiscountRecords(val, this.page.size)
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.list-apply-room-discount-record-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .header-title {
        margin-left: 10px;
        font-weight: bold;
      }

      .header-tools {
        display: flex;
        align-items: center;
      }
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>