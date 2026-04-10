<template>
  <div class="margin-top">
    <el-table :data="ownerDetailComplaintInfo.complaints" style="width: 100%">
      <el-table-column prop="typeName" :label="$t('complaint.table.type')" align="center"></el-table-column>
      <el-table-column :label="$t('complaint.table.room')" align="center">
        <template slot-scope="scope">
          {{scope.row.roomName}}
        </template>
      </el-table-column>
      <el-table-column prop="complaintName" :label="$t('complaint.table.contact')" align="center"></el-table-column>
      <el-table-column prop="tel" :label="$t('complaint.table.phone')" align="center"></el-table-column>
      <el-table-column prop="stateName" :label="$t('complaint.table.status')" align="center"></el-table-column>
      <el-table-column prop="currentUserName" :label="$t('complaint.table.handler')" align="center">
        <template slot-scope="scope">
          {{scope.row.currentUserName == '' ? $t('common.none'):scope.row.currentUserName}}
        </template>
      </el-table-column>
      <el-table-column prop="currentUserTel" :label="$t('complaint.table.handlerPhone')" align="center">
        <template slot-scope="scope">
          {{scope.row.currentUserTel == '' ? $t('common.none'):scope.row.currentUserTel}}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('complaint.table.createTime')" align="center"></el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="200">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" @click="_openComplaintDetailModel(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      layout="total, prev, pager, next, jumper">
    </el-pagination>

    <complaint-detail ref="complaintDetail"></complaint-detail>
    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import ComplaintDetail from '@/components/oa/complaintDetail'
import ViewImage from '@/components/system/viewImage'
import { listComplaints, listRunWorkflowImage } from '@/api/owner/ownerDetailComplaintApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailComplaint',
  components: {
    ComplaintDetail,
    ViewImage
  },
  data() {
    return {
      ownerDetailComplaintInfo: {
        complaints: [],
        ownerId: '',
        ownerName: '',
        link: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName, link) {
      this.ownerDetailComplaintInfo.ownerId = ownerId
      this.ownerDetailComplaintInfo.ownerName = ownerName
      this.ownerDetailComplaintInfo.link = link
      this._loadOwnerDetailComplaintData(1, this.pagination.pageSize)
    },
   async _loadOwnerDetailComplaintData(page, row) {
      const params = {
        communityId: getCommunityId(),
        memberId: this.ownerDetailComplaintInfo.ownerId,
        tel: this.ownerDetailComplaintInfo.link,
        page: page,
        row: row
      }

     const res = await listComplaints(params)

     this.ownerDetailComplaintInfo.complaints = res.data
     this.pagination.total = res.total
    },
    _openComplaintDetailModel(complaint) {
      this.$refs.complaintDetail.open(complaint)
    },
    _openRunWorkflowImage(complaint) {
      const params = {
        communityId: getCommunityId(),
        businessKey: complaint.complaintId
      }

      listRunWorkflowImage(params).then(response => {
        if (response.code != '0') {
          this.$message.error(response.msg)
          return
        }
        this.$refs.viewImage.open('data:image/png;base64,' + response.data)
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailComplaintData(val, this.pagination.pageSize)
    }
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailComplaintInfo.ownerId = data.ownerId
      this._loadOwnerDetailComplaintData(1, this.pagination.pageSize)
    })
  }
}
</script>