<template>
  <div>
    <el-table
      :data="aRoomDetailHisOwnerInfo.owners"
      border
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column prop="name" :label="$t('aRoomDetailHisOwner.ownerName')" align="center" />
      <el-table-column prop="link" :label="$t('aRoomDetailHisOwner.ownerPhone')" align="center" />
      <el-table-column prop="startTime" :label="$t('aRoomDetailHisOwner.startTime')" align="center" />
      <el-table-column prop="endTime" :label="$t('aRoomDetailHisOwner.endTime')" align="center" />
      <el-table-column prop="createTime" :label="$t('aRoomDetailHisOwner.createTime')" align="center" />
      <el-table-column :label="$t('aRoomDetailHisOwner.status')" align="center">
        <template slot-scope="scope">
          {{scope.row.statusCd === '0' ? $t('aRoomDetailHisOwner.active') : $t('aRoomDetailHisOwner.inactive')}}
        </template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top">
      <el-col :span="24" class="text-right">
        <el-pagination
          @current-change="handlePageChange"
          :current-page="pagination.currentPage"
          :page-size="pagination.pageSize"
          layout="total, prev, pager, next"
          :total="pagination.total"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminRoomHisOwner } from '@/api/aCommunity/aRoomDetailHisOwnerApi'

export default {
  name: 'ARoomDetailHisOwner',
  data() {
    return {
      aRoomDetailHisOwnerInfo: {
        owners: [],
        ownerId: '',
        roomId: ''
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
      if (!data.roomId) return
      this.clearData()
      this.aRoomDetailHisOwnerInfo.roomId = data.roomId
      this.loadOwnerData()
    },
    loadOwnerData() {
      const params = {
        page: this.pagination.currentPage,
        row: this.pagination.pageSize,
        roomId: this.aRoomDetailHisOwnerInfo.roomId
      }

      queryAdminRoomHisOwner(params).then(response => {
        this.aRoomDetailHisOwnerInfo.owners = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('Failed to load owner history:', error)
      })
    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this.loadOwnerData()
    },
    clearData() {
      this.aRoomDetailHisOwnerInfo = {
        owners: [],
        ownerId: '',
        roomId: ''
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
.text-right {
  text-align: right;
}
</style>