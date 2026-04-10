<template>
  <div>
    <div class="flex justify-between">
      <div></div>
      <div v-if="simplifyOwnerComplaintInfo.roomId != ''">
        <el-button type="primary" size="small" @click="_openAddComplaintModal">
          <i class="el-icon-plus"></i>{{ $t('simplifyOwnerComplaint.complaint') }}
        </el-button>
      </div>
    </div>

    <el-table :data="simplifyOwnerComplaintInfo.complaints" style="width: 100%; margin-top: 10px" border>
      <el-table-column prop="typeName" :label="$t('simplifyOwnerComplaint.complaintType')"
        align="center"></el-table-column>
      <el-table-column :label="$t('simplifyOwnerComplaint.house')" align="center">
        <template #default="{ row }">
          {{ row.roomName }}
        </template>
      </el-table-column>
      <el-table-column prop="complaintName" :label="$t('simplifyOwnerComplaint.complainant')"
        align="center"></el-table-column>
      <el-table-column prop="tel" :label="$t('simplifyOwnerComplaint.complainantPhone')"
        align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('simplifyOwnerComplaint.complaintStatus')"
        align="center"></el-table-column>
      <el-table-column prop="currentUserName" :label="$t('simplifyOwnerComplaint.handler')" align="center">
        <template #default="{ row }">
          {{ row.currentUserName == '' ? $t('simplifyOwnerComplaint.none') : row.currentUserName }}
        </template>
      </el-table-column>
      <el-table-column prop="currentUserTel" :label="$t('simplifyOwnerComplaint.handlerPhone')" align="center">
        <template #default="{ row }">
          {{ row.currentUserTel == '' ? $t('simplifyOwnerComplaint.none') : row.currentUserTel }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('simplifyOwnerComplaint.operation')" align="center">
        <template #default="{ row }">
          <el-button type="text" @click="_openComplaintDetailModel(row)">
            {{ $t('simplifyOwnerComplaint.details') }}
          </el-button>
          <el-button type="text" @click="_openRunWorkflowImage(row)">
            {{ $t('simplifyOwnerComplaint.flowChart') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
      layout="total, prev, pager, next" :total="total">
    </el-pagination>

    <complaint-detail ref="complaintDetail"></complaint-detail>
    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listComplaints, listRunWorkflowImage } from '@/api/simplify/simplifyOwnerComplaintApi'
import ComplaintDetail from '@/components/oa/complaintDetail'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'SimplifyOwnerComplaint',
  components: {
    ComplaintDetail,
    ViewImage
  },
  data() {
    return {
      simplifyOwnerComplaintInfo: {
        complaints: [],
        ownerId: '',
        roomId: '',
        roomName: '',
        total: 0,
        records: 1
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()

  },
  methods: {
    open(param) {
      this.handleSwitch(param)
    },
    handleSwitch(param) {
      if (param.roomId == '') return
      this.clearSimplifyOwnerComplaintInfo()
      Object.assign(this.simplifyOwnerComplaintInfo, param)
      this.listSimplifyOwnerComplaint(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listSimplifyOwnerComplaint(val, this.pageSize)
    },
    listSimplifyOwnerComplaint(page, row) {
      const params = {
        page,
        row,
        communityId: this.communityId,
        roomId: this.simplifyOwnerComplaintInfo.roomId
      }

      listComplaints(params).then(res => {
        this.simplifyOwnerComplaintInfo.complaints = res.data
        this.total = res.total
      })
    },
    _openComplaintDetailModel(complaint) {
      this.$refs.complaintDetail.open(complaint)
    },
    _openRunWorkflowImage(complaint) {
      const params = {
        communityId: this.communityId,
        businessKey: complaint.complaintId
      }

      listRunWorkflowImage(params).then(res => {
        if (res.code == 0) {
          this.$refs.viewImage.show({
            url: `data:image/png;base64,${res.data}`
          })
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    clearSimplifyOwnerComplaintInfo() {
      this.simplifyOwnerComplaintInfo = {
        complaints: [],
        ownerId: '',
        roomId: '',
        roomName: ''
      }
    },
    _openAddComplaintModal() {
      this.$router.push(`/views/oa/addComplaint?roomId=${this.simplifyOwnerComplaintInfo.roomId}&roomName=${this.simplifyOwnerComplaintInfo.roomName}`)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 10px;
}
</style>