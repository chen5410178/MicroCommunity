<template>
  <el-dialog :visible.sync="visible" :title="$t('ownerOweFees.title')" width="80%" top="5vh" custom-class="custom-modal"
    @close="handleClose">
    <el-table :data="ownerOweFeesInfo.fees" stripe border style="width: 100%; margin-top: 15px">
      <el-table-column prop="payerObjName" :label="$t('ownerOweFees.payerObj')" align="center" />
      <el-table-column prop="ownerName" :label="$t('ownerOweFees.ownerName')" align="center" />
      <el-table-column prop="ownerTel" :label="$t('ownerOweFees.phone')" align="center" />
      <el-table-column prop="endTime" :label="$t('ownerOweFees.startTime')" align="center" />
      <el-table-column prop="deadlineTime" :label="$t('ownerOweFees.endTime')" align="center" />
      <el-table-column prop="amountOwed" :label="`${$t('ownerOweFees.total')} (${$t('ownerOweFees.unit')})`"
        align="center" />
      <el-table-column prop="updateTime" :label="$t('ownerOweFees.updateTime')" align="center" />
    </el-table>

    <el-pagination v-if="pagination.total > 0" background layout="prev, pager, next" :total="pagination.total"
      :page-size="pagination.pageSize" :current-page="pagination.currentPage" @current-change="handlePageChange"
      style="margin-top: 20px; text-align: right" />
  </el-dialog>
</template>

<script>
import { getOwnerOweFees } from '@/api/room/ownerOweFeesApi'

export default {
  name: 'OwnerOweFees',
  data() {
    return {
      visible: false,
      ownerOweFeesInfo: {
        fees: [],
        ownerId: ''
      },
      pagination: {
        total: 0,
        pageSize: 10,
        currentPage: 1
      }
    }
  },
  methods: {
    open(ownerId) {
      this.ownerOweFeesInfo.ownerId = ownerId
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
          ownerId: this.ownerOweFeesInfo.ownerId
        }

        const response = await getOwnerOweFees(params)
        this.ownerOweFeesInfo.fees = response.data
        this.pagination.total = response.total
      } catch (error) {
        console.error('加载欠费信息失败:', error)
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