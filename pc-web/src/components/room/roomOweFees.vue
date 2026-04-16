<template>
  <el-dialog :visible.sync="visible" :title="$t('roomOweFees.title')" width="80%" top="5vh" custom-class="custom-modal"
    @close="handleClose">
    <el-table :data="roomOweFeesInfo.fees" stripe border style="width: 100%; margin-top: 15px">
      <el-table-column prop="payerObjName" :label="$t('roomOweFees.payerObj')" align="center" />
      <el-table-column prop="ownerName" :label="$t('roomOweFees.ownerName')" align="center" />
      <el-table-column prop="ownerTel" :label="$t('roomOweFees.phone')" align="center" />
      <el-table-column prop="endTime" :label="$t('roomOweFees.startTime')" align="center" />
      <el-table-column prop="deadlineTime" :label="$t('roomOweFees.endTime')" align="center" />
      <el-table-column prop="amountOwed" :label="`${$t('roomOweFees.total')} (${$t('roomOweFees.unit')})`"
        align="center" />
      <el-table-column prop="updateTime" :label="$t('roomOweFees.updateTime')" align="center" />
    </el-table>

    <el-pagination v-if="pagination.total > 0" background layout="prev, pager, next" :total="pagination.total"
      :page-size="pagination.pageSize" :current-page="pagination.currentPage" @current-change="handlePageChange"
      style="margin-top: 20px; text-align: right" />
  </el-dialog>
</template>

<script>
import { getRoomOweFees } from '@/api/room/roomOweFeesApi'

export default {
  name: 'RoomOweFees',
  data() {
    return {
      visible: false,
      roomOweFeesInfo: {
        fees: [],
        roomId: ''
      },
      pagination: {
        total: 0,
        pageSize: 10,
        currentPage: 1
      }
    }
  },
  methods: {
    open(roomId) {
      this.roomOweFeesInfo.roomId = roomId
      this.pagination.currentPage = 1
      this.visible = true
      this.loadData()
    },
    handleClose() {
      this.visible = false
    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this.loadData()
    },
    async loadData() {
      try {
        const params = {
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          communityId: this.getCommunityId(),
          payerObjId: this.roomOweFeesInfo.roomId,
          payerObjType: '3333'
        }

        const response = await getRoomOweFees(params)
        this.roomOweFeesInfo.fees = response.data
        this.pagination.total = response.total
      } catch (error) {
        console.error('加载房屋欠费信息失败:', error)
        this.$message.error(this.$t('common.loadFailed'))
      }
    },

  }
}
</script>

<style scoped>
.custom-modal>>>.el-dialog__body {
  padding: 15px 20px;
}
</style>