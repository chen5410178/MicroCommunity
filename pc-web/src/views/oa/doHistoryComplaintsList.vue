<template>
  <div class="do-history-complaints-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('doHistoryComplaints.title') }}</span>
        <div class="card-header-actions">
          <el-button size="small" @click="handleGoBack">{{ $t('doHistoryComplaints.back') }}</el-button>
          <el-button type="primary" size="small" @click="queryAuditOrders">{{ $t('doHistoryComplaints.refresh')
            }}</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="complaintId" :label="$t('doHistoryComplaints.orderId')" align="center" />
        <el-table-column prop="typeName" :label="$t('doHistoryComplaints.complaintType')" align="center" />
        <el-table-column prop="roomName" :label="$t('doHistoryComplaints.house')" align="center" />
        <el-table-column prop="complaintName" :label="$t('doHistoryComplaints.complainer')" align="center" />
        <el-table-column prop="tel" :label="$t('doHistoryComplaints.complaintPhone')" align="center" />
        <el-table-column prop="stateName" :label="$t('doHistoryComplaints.complaintStatus')" align="center" />
        <el-table-column prop="createTime" :label="$t('doHistoryComplaints.createTime')" align="center" />
        <el-table-column :label="$t('doHistoryComplaints.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button size="mini" @click="openComplaintDetail(scope.row)">
              {{ $t('doHistoryComplaints.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { listAuditHistoryComplaints } from '@/api/oa/doHistoryComplaintsApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DoHistoryComplaintsList',
  data() {
    return {
      loading: false,
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId
        }
        const { data, total } = await listAuditHistoryComplaints(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('doHistoryComplaints.fetchError'))
      } finally {
        this.loading = false
      }
    },
    queryAuditOrders() {
      this.pagination.current = 1
      this.getList()
    },
    handleGoBack() {
      this.$router.go(-1)
    },
    openComplaintDetail(complaint) {
      this.$router.push({
        path: '/pages/complaint/complaintDetail',
        query: { complaintId: complaint.complaintId }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.do-history-complaints-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
    display: flex;
    gap: 10px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>