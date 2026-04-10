<template>
  <el-dialog :visible.sync="dialogVisible" :title="$t('ownerComplaints.title')" width="80%" top="5vh"
    @close="handleClose">
    <el-row>
      <el-col :span="24">
        <div class="table-container">
          <el-table :data="ownerComplaintsInfo.complaints" border stripe>
            <el-table-column prop="typeCdName" :label="$t('ownerComplaints.type')" align="center" />
            <el-table-column :label="$t('ownerComplaints.room')" align="center">
              <template #default="{ row }">
                {{ row.floorNum }}-{{ row.unitNum }}-{{ row.roomNum }}
              </template>
            </el-table-column>
            <el-table-column prop="complaintName" :label="$t('ownerComplaints.contact')" align="center" />
            <el-table-column prop="tel" :label="$t('ownerComplaints.phone')" align="center" />
            <el-table-column prop="stateName" :label="$t('ownerComplaints.status')" align="center" />
            <el-table-column :label="$t('ownerComplaints.handler')" align="center">
              <template #default="{ row }">
                {{ row.currentUserName || $t('ownerComplaints.none') }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('ownerComplaints.handlerPhone')" align="center">
              <template #default="{ row }">
                {{ row.currentUserTel || $t('ownerComplaints.none') }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('ownerComplaints.createTime')" align="center" />
          </el-table>
        </div>
        <el-pagination background layout="prev, pager, next, sizes, total" :total="totalCount" :page-size="pageSize"
          :current-page="currentPage" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { listComplaints } from '@/api/room/ownerComplaintsApi'

export default {
  name: 'OwnerComplaints',
  data() {
    return {
      dialogVisible: false,
      ownerComplaintsInfo: {
        complaints: [],
        ownerId: ''
      },
      totalCount: 0,
      pageSize: 10,
      currentPage: 1
    }
  },
  methods: {
    open(ownerId) {
      this.ownerComplaintsInfo.ownerId = ownerId
      this.dialogVisible = true
      this._loadOwnerComplaintInfo(1, this.pageSize)
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSizeChange(size) {
      this.pageSize = size
      this._loadOwnerComplaintInfo(this.currentPage, size)
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this._loadOwnerComplaintInfo(page, this.pageSize)
    },
    async _loadOwnerComplaintInfo(page, size) {
      try {
        const params = {
          page: page,
          row: size,
          communityId: this.getCommunityId(),
          ownerId: this.ownerComplaintsInfo.ownerId
        }

        const response = await listComplaints(params)
        this.ownerComplaintsInfo.complaints = response.data
        this.totalCount = response.total
        this.currentPage = page
      } catch (error) {
        console.error('加载业主投诉信息失败:', error)
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