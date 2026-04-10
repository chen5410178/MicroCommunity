<template>
  <div class="coupon-rule-fees">
    <el-row class="operation-row">
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" @click="handleQuery" class="query-btn">
          {{ $t('common.query') }}
        </el-button>
        <el-button type="primary" size="small" @click="openAddModal" class="add-btn">
          <i class="el-icon-plus"></i>
          {{ $t('common.add') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="crfId" :label="$t('couponRuleFee.id')" align="center" width="100">
      </el-table-column>
      <el-table-column prop="feeConfigName" :label="$t('couponRuleFee.fee')" align="center">
      </el-table-column>
      <el-table-column :label="$t('couponRuleFee.payTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.payStartTime }} ~ {{ scope.row.payEndTime }}
        </template>
      </el-table-column>
      <el-table-column prop="payMonth" :label="$t('couponRuleFee.payMonth')" align="center">
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="200">
        <template slot-scope="scope">
          <!-- <el-button size="mini" @click="openEditModal(scope.row)">
            {{ $t('common.edit') }}
          </el-button> -->
          <el-button size="mini" type="danger" @click="openDeleteModal(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total">
    </el-pagination>

    <add-coupon-rule-fee ref="addCouponRuleFee" :rule-id="ruleId" @success="handleSuccess" />
    <delete-coupon-rule-fee ref="deleteCouponRuleFee" @success="handleSuccess" />

  </div>
</template>

<script>
import { listCouponRuleFee } from '@/api/scm/couponRuleApi'
import AddCouponRuleFee from './AddCouponRuleFee'
import DeleteCouponRuleFee from './DeleteCouponRuleFee'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CouponRuleFees',
  components: {
    AddCouponRuleFee,
    DeleteCouponRuleFee
  },
  
  data() {
    return {
      loading: false,
      tableData: [],
      ruleId:'',
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {

    open(ruleId){
      this.ruleId = ruleId
      this.getList()
    },
    async getList() {
      if (!this.ruleId) return

      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ruleId: this.ruleId,
          communityId: getCommunityId()
        }
        const { data, total } = await listCouponRuleFee(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to get coupon rule fees:', error)
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    openAddModal() {
      this.$refs.addCouponRuleFee.open()
    },
    openEditModal(row) {
      // TODO: Implement edit modal
      this.$refs.editCouponRuleFee.open(row)
    },
    openDeleteModal(row) {
      this.$refs.deleteCouponRuleFee.open(row)

    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.coupon-rule-fees {
  .operation-row {
    margin-bottom: 20px;

    .query-btn {
      margin-right: 10px;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>