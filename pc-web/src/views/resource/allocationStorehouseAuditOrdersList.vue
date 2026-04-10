<template>
  <div class="allocation-storehouse-audit-orders-container animated fadeInRight">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('allocationStorehouseAuditOrders.title') }}</span>
            <div class="card-header-actions">
              <el-button type="primary" size="small" @click="goBack">
                <i class="el-icon-close"></i>
                {{ $t('allocationStorehouseAuditOrders.back') }}
              </el-button>
              <el-button type="primary" size="small" @click="queryAuditOrders">
                <i class="el-icon-refresh"></i>
                {{ $t('allocationStorehouseAuditOrders.refresh') }}
              </el-button>
            </div>
          </div>

          <el-table :data="allocationStorehouseAuditOrdersInfo.auditOrders" border style="width: 100%"
            v-loading="loading">
            <el-table-column prop="applyId" :label="$t('allocationStorehouseAuditOrders.scheduleNo')" align="center" />
            <el-table-column prop="applyCount" :label="$t('allocationStorehouseAuditOrders.allocationCount')"
              align="center" />
            <el-table-column prop="startUserName" :label="$t('allocationStorehouseAuditOrders.applicant')"
              align="center" />
            <el-table-column prop="stateName" :label="$t('allocationStorehouseAuditOrders.status')" align="center" />
            <el-table-column prop="createTime" :label="$t('allocationStorehouseAuditOrders.time')" align="center" />
            <el-table-column :label="$t('allocationStorehouseAuditOrders.operation')" align="center" width="250">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" @click="toDetail(scope.row)">
                    {{ $t('allocationStorehouseAuditOrders.view') }}
                  </el-button>
                  <el-button size="mini"
                    v-if="scope.row.startUserId === allocationStorehouseAuditOrdersInfo.currentUserId"
                    @click="openEditPurchaseModel(scope.row)">
                    {{ $t('allocationStorehouseAuditOrders.edit') }}
                  </el-button>
                  <el-button size="mini" v-if="scope.row.curTaskName === '仓库管理员'"
                    @click="allocationStorehouseEnter(scope.row)">
                    {{ $t('allocationStorehouseAuditOrders.allocation') }}
                  </el-button>
                  <el-button size="mini" v-else @click="openAuditOrderModel(scope.row)">
                    {{ $t('allocationStorehouseAuditOrders.approve') }}
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" class="pagination">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAllocationStoreAuditOrders, listWorkflowStepStaffs } from '@/api/resource/allocationStorehouseAuditOrdersApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'AllocationStorehouseAuditOrdersList',
  data() {
    return {
      loading: false,
      communityId: '',
      allocationStorehouseAuditOrdersInfo: {
        auditOrders: [],
        currentUserId: '',
        conditions: {
          AuditOrdersId: '',
          userName: '',
          auditLink: '',
          page: 1,
          row: 10
        },
        procure: false,
        audit: '1'
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.allocationStorehouseAuditOrdersInfo.currentUserId = getUserId()
    this.listAuditOrders()
    this.loadStepStaff()
  },
  methods: {
    async listAuditOrders() {
      this.loading = true
      try {
        const params = {
          ...this.allocationStorehouseAuditOrdersInfo.conditions,
          communityId: this.communityId
        }
        const res = await listAllocationStoreAuditOrders(params)
        this.allocationStorehouseAuditOrdersInfo.auditOrders = res.data
        this.pagination.total = res.total
      } catch (error) {
        console.error('获取审核订单列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadStepStaff() {
      try {
        const params = {
          page: 1,
          row: 1,
          staffId: this.allocationStorehouseAuditOrdersInfo.currentUserId,
          staffRole: '3003',
          requestType: 'allocationHandle'
        }
        const res = await listWorkflowStepStaffs(params)
        if (res.data.length > 0) {
          this.allocationStorehouseAuditOrdersInfo.procure = true
        }
      } catch (error) {
        console.error('获取步骤员工失败:', error)
      }
    },
    queryAuditOrders() {
      this.pagination.currentPage = 1
      this.listAuditOrders()
    },
    openAuditOrderModel(auditOrder) {
      this.$router.push({
        path: '/pages/common/allocationStorehouseDetail',
        query: {
          applyId: auditOrder.applyId,
          action: 'audit',
          taskId: auditOrder.taskId,
          flowId: auditOrder.flowId
        }
      })
    },
    openEditPurchaseModel(auditOrder) {
      this.$router.push({
        path: '/pages/resource/editAllocationStorehouseApply',
        query: { applyId: auditOrder.applyId }
      })
    },
    allocationStorehouseEnter(auditOrder) {
      this.$router.push({
        path: '/pages/resource/allocationStorehouseEnter',
        query: {
          applyId: auditOrder.applyId,
          taskId: auditOrder.taskId
        }
      })
    },
    toDetail(item) {
      this.$router.push({
        path: '/pages/common/allocationStorehouseDetail',
        query: { applyId: item.applyId }
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.allocationStorehouseAuditOrdersInfo.conditions.row = val
      this.listAuditOrders()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.allocationStorehouseAuditOrdersInfo.conditions.page = val
      this.listAuditOrders()
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-storehouse-audit-orders-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .card-header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }

  .el-button-group {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
  }
}
</style>