<template>
  <el-dialog :visible.sync="visible" :title="$t('roomContracts.title')" width="90%" top="5vh" custom-class="custom-modal"
    @close="handleClose">
    <el-table :data="roomContractsInfo.contracts" stripe border style="width: 100%; margin-top: 15px">
      <el-table-column prop="contractName" :label="$t('roomContracts.contractName')" align="center" />
      <el-table-column prop="contractCode" :label="$t('roomContracts.contractCode')" align="center" />
      <el-table-column prop="parentContractCode" :label="$t('roomContracts.parentContractCode')" align="center">
        <template slot-scope="scope">
          {{ scope.row.parentContractCode || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="contractTypeName" :label="$t('roomContracts.contractType')" align="center" />
      <el-table-column prop="operator" :label="$t('roomContracts.operator')" align="center" />
      <el-table-column prop="amount" :label="$t('roomContracts.amount')" align="center" />
      <el-table-column prop="startTime" :label="$t('roomContracts.startTime')" align="center" />
      <el-table-column prop="endTime" :label="$t('roomContracts.endTime')" align="center" />
      <el-table-column prop="createTime" :label="$t('roomContracts.createTime')" align="center" />
      <el-table-column prop="stateName" :label="$t('roomContracts.state')" align="center" />
    </el-table>

    <el-pagination v-if="pagination.total > 0" background layout="prev, pager, next" :total="pagination.total"
      :page-size="pagination.pageSize" :current-page="pagination.currentPage" @current-change="handlePageChange"
      style="margin-top: 20px; text-align: right" />
  </el-dialog>
</template>

<script>
import { getRoomContracts } from '@/api/room/roomContractsApi'

export default {
  name: 'RoomContracts',
  data() {
    return {
      visible: false,
      roomContractsInfo: {
        contracts: [],
        ownerId: '',
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
    open(params) {
      this.roomContractsInfo.ownerId = params.ownerId || ''
      this.roomContractsInfo.roomId = params.roomId || ''
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
          objId: this.roomContractsInfo.ownerId
        }

        if (this.roomContractsInfo.roomId) {
          params.roomId = this.roomContractsInfo.roomId
        }

        const response = await getRoomContracts(params, this.roomContractsInfo.roomId)
        this.roomContractsInfo.contracts = response.data
        this.pagination.total = response.total
      } catch (error) {
        console.error('加载合同信息失败:', error)
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