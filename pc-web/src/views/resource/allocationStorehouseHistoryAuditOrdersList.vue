<template>
  <div class="allocation-storehouse-history-audit-orders-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('allocationStorehouseHistoryAuditOrders.title') }}</span>
        <div class="header-buttons">
          <el-button type="primary" size="small" @click="goBack">
            <i class="el-icon-close"></i>
            {{ $t('allocationStorehouseHistoryAuditOrders.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="queryAuditOrders">
            <i class="el-icon-refresh"></i>
            {{ $t('allocationStorehouseHistoryAuditOrders.refresh') }}
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="auditOrders"
        border
        style="width: 100%"
        class="table-wrapper"
      >
        <el-table-column
          prop="applyId"
          :label="$t('allocationStorehouseHistoryAuditOrders.allocationNumber')"
          align="center"
        />
        <el-table-column
          prop="applyCount"
          :label="$t('allocationStorehouseHistoryAuditOrders.allocationCount')"
          align="center"
        />
        <el-table-column
          prop="startUserName"
          :label="$t('allocationStorehouseHistoryAuditOrders.applicant')"
          align="center"
        />
        <el-table-column
          prop="stateName"
          :label="$t('allocationStorehouseHistoryAuditOrders.status')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('allocationStorehouseHistoryAuditOrders.time')"
          align="center"
        />
        <el-table-column
          :label="$t('allocationStorehouseHistoryAuditOrders.operation')"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="toDetail(scope.row)"
            >
              {{ $t('allocationStorehouseHistoryAuditOrders.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination-wrapper"
      />
    </el-card>
  </div>
</template>

<script>
import { listAllocationStoreHisAuditOrders } from '@/api/resource/allocationStorehouseHistoryAuditOrdersApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AllocationStorehouseHistoryAuditOrdersList',
  data() {
    return {
      loading: false,
      auditOrders: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      conditions: {
        auditOrdersId: '',
        userName: '',
        auditLink: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.listAuditOrders()
  },
  methods: {
    async listAuditOrders() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          ...this.conditions
        }
        const { data, total } = await listAllocationStoreHisAuditOrders(params)
        this.auditOrders = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('allocationStorehouseHistoryAuditOrders.fetchError'))
      } finally {
        this.loading = false
      }
    },
    queryAuditOrders() {
      this.page.current = 1
      this.listAuditOrders()
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
      this.page.size = val
      this.listAuditOrders()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listAuditOrders()
    }
  }
}
</script>

<style lang="scss" scoped>
.allocation-storehouse-history-audit-orders-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .header-buttons {
    float: right;
    margin-top: -5px;
  }

  .table-wrapper {
    margin-top: 20px;
  }

  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }
}
</style>