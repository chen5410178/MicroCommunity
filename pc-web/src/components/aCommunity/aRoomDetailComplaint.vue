<template>
  <div class="margin-top">
    <el-table :data="aRoomDetailComplaintInfo.complaints" border style="width: 100%" class="margin-top">
      <el-table-column prop="typeCdName" :label="$t('aRoomDetailComplaint.type')" align="center" />
      <el-table-column :label="$t('aRoomDetailComplaint.room')" align="center">
        <template slot-scope="scope">
          {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
        </template>
      </el-table-column>
      <el-table-column prop="complaintName" :label="$t('aRoomDetailComplaint.contact')" align="center" />
      <el-table-column prop="tel" :label="$t('aRoomDetailComplaint.phone')" align="center" />
      <el-table-column prop="stateName" :label="$t('aRoomDetailComplaint.status')" align="center" />
      <el-table-column :label="$t('aRoomDetailComplaint.handler')" align="center">
        <template slot-scope="scope">
          {{ scope.row.currentUserName || $t('aRoomDetailComplaint.none') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('aRoomDetailComplaint.handlerPhone')" align="center">
        <template slot-scope="scope">
          {{ scope.row.currentUserTel || $t('aRoomDetailComplaint.none') }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('aRoomDetailComplaint.createTime')" align="center" />
      <el-table-column :label="$t('aRoomDetailComplaint.actions')" align="center" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleOpenComplaintDetail(scope.row)">
            {{ $t('aRoomDetailComplaint.detail') }}
          </el-button>
          <el-button size="mini" @click="handleOpenWorkflowImage(scope.row)">
            {{ $t('aRoomDetailComplaint.flowChart') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="margin-top">
      <el-col :span="24" class="text-right">
        <el-pagination @current-change="handlePageChange" :current-page="pagination.currentPage"
          :page-size="pagination.pageSize" layout="total, prev, pager, next" :total="pagination.total" />
      </el-col>
    </el-row>

    <complaint-detail ref="complaintDetail" />
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { listAdminComplaints } from '@/api/aCommunity/aRoomDetailComplaintApi'
import ComplaintDetail from '@/components/oa/complaintDetail'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'ARoomDetailComplaint',
  components: {
    ComplaintDetail,
    ViewImage
  },
  data() {
    return {
      aRoomDetailComplaintInfo: {
        complaints: [],
        ownerId: '',
        name: '',
        currentAppUserId: ''
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
      this.aRoomDetailComplaintInfo.ownerId = data.ownerId
      this.loadComplaintData()
    },
    loadComplaintData() {
      const params = {
        memberId: this.aRoomDetailComplaintInfo.ownerId,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      listAdminComplaints(params).then(response => {
        this.aRoomDetailComplaintInfo.complaints = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('Failed to load complaints:', error)
      })
    },
    handlePageChange(currentPage) {
      this.pagination.currentPage = currentPage
      this.loadComplaintData()
    },
    handleOpenComplaintDetail(complaint) {
      this.$refs.complaintDetail.open(complaint)
    },
    handleOpenWorkflowImage(complaint) {
      this.$refs.viewImage.open(complaint.workflowImageUrl)
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