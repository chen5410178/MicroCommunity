<template>
  <div class="uodo-complaints-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between ">
        <span>{{ $t('uodoComplaints.title') }}</span>
        <div class="card-header-actions">
          <el-button size="small" @click="handleGoBack">{{ $t('uodoComplaints.back') }}</el-button>
          <el-button type="primary" size="small" @click="handleRefresh">{{ $t('uodoComplaints.refresh') }}</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="complaintId" :label="$t('uodoComplaints.orderNumber')" align="center" />
        <el-table-column prop="typeName" :label="$t('uodoComplaints.complaintType')" align="center" />
        <el-table-column prop="roomName" :label="$t('uodoComplaints.house')" align="center" />
        <el-table-column prop="complaintName" :label="$t('uodoComplaints.complainant')" align="center" />
        <el-table-column prop="tel" :label="$t('uodoComplaints.complaintPhone')" align="center" />
        <el-table-column prop="stateName" :label="$t('uodoComplaints.complaintStatus')" align="center" />
        <el-table-column prop="createTime" :label="$t('uodoComplaints.createTime')" align="center" />
        <el-table-column :label="$t('uodoComplaints.operation')" align="center" width="180">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="handleProcess(scope.row)">{{ $t('uodoComplaints.process') }}</el-button>
              <el-button size="mini" @click="handleDetail(scope.row)">{{ $t('uodoComplaints.detail') }}</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="pagination.currentPage" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <doing-complaint ref="doingComplaint" @success="handleSuccess" />
  </div>
</template>

<script>
import { listAuditComplaints } from '@/api/oa/uodoComplaintsApi'
import DoingComplaint from '@/components/oa/doingComplaint'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'UodoComplaintsList',
  components: {
    DoingComplaint
  },
  data() {
    return {
      loading: false,
      tableData: [],
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      conditions: {
        communityId: '',
        userName: '',
        auditLink: ''
      }
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
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          communityId: this.communityId,
          ...this.conditions
        }
        const { data, total } = await listAuditComplaints(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('uodoComplaints.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleGoBack() {
      this.$router.go(-1)
    },
    handleRefresh() {
      this.pagination.currentPage = 1
      this.getList()
    },
    handleProcess(row) {
      this.$refs.doingComplaint.open(row)
    },
    handleDetail(row) {
      this.$router.push(`/pages/complaint/complaintDetail?complaintId=${row.complaintId}`)
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.uodo-complaints-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>