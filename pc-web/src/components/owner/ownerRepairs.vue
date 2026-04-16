<template>
  <el-dialog :visible.sync="dialogVisible" :title="$t('ownerRepairs.title')" width="80%" top="5vh" @close="handleClose">
    <el-row>
      <el-col :span="24">
        <div class="table-container">
          <el-table :data="ownerRepairsInfo.repairs" border stripe>
            <el-table-column prop="repairId" :label="$t('ownerRepairs.workOrder')" align="center" />
            <el-table-column prop="repairObjName" :label="$t('ownerRepairs.location')" align="center" />
            <el-table-column prop="repairTypeName" :label="$t('ownerRepairs.repairType')" align="center" />
            <el-table-column prop="repairName" :label="$t('ownerRepairs.reporter')" align="center" />
            <el-table-column prop="tel" :label="$t('ownerRepairs.contact')" align="center" />
            <el-table-column prop="appointmentTime" :label="$t('ownerRepairs.appointmentTime')" align="center" />
            <el-table-column :label="$t('ownerRepairs.status')" align="center">
              <template #default="{ row }">
                <span v-if="row.state === '1800' && (row.returnVisitFlag === '001' || row.returnVisitFlag === '002')">
                  {{ row.stateName }}{{ $t('ownerRepairs.taskProcessed') }}
                </span>
                <span v-else>
                  {{ row.stateName }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-pagination background layout="prev, pager, next, sizes, total" :total="totalCount" :page-size="pageSize"
          :current-page="currentPage" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { listOwnerRepairs } from '@/api/room/ownerRepairsApi'

export default {
  name: 'OwnerRepairs',
  data() {
    return {
      dialogVisible: false,
      ownerRepairsInfo: {
        repairs: [],
        ownerId: ''
      },
      totalCount: 0,
      pageSize: 10,
      currentPage: 1
    }
  },
  methods: {
    open(ownerId) {
      this.ownerRepairsInfo.ownerId = ownerId
      this.dialogVisible = true
      this._loadOwnerRepairInfo(1, this.pageSize)
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSizeChange(size) {
      this.pageSize = size
      this._loadOwnerRepairInfo(this.currentPage, size)
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this._loadOwnerRepairInfo(page, this.pageSize)
    },
    async _loadOwnerRepairInfo(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          communityId: this.getCommunityId(),
          ownerId: this.ownerRepairsInfo.ownerId
        }

        const response = await listOwnerRepairs(params)
        this.ownerRepairsInfo.repairs = response.data
        this.totalCount = response.total
        this.currentPage = page
      } catch (error) {
        console.error('加载业主报修信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.table-container {
  margin-top: 15px;
}
</style>