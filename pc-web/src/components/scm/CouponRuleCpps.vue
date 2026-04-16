<template>
  <div class="coupon-rule-cpps">
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
      <el-table-column prop="crcId" :label="$t('couponRuleCpps.id')" align="center" width="100">
      </el-table-column>
      <el-table-column prop="couponName" :label="$t('couponRuleCpps.coupon')" align="center">
      </el-table-column>
      <el-table-column prop="quantity" :label="$t('couponRuleCpps.quantity')" align="center">
      </el-table-column>
      <el-table-column prop="giftFrequencyName" :label="$t('couponRuleCpps.frequency')" align="center">
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="openEditModal(scope.row)">
            {{ $t('common.edit') }}
          </el-button>
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

    <add-coupon-rule-cpps ref="addCouponRuleCpps" :rule-id="ruleId" @success="handleSuccess" />
    <edit-coupon-rule-cpps ref="editCouponRuleCpps" @success="handleSuccess" />
    <delete-coupon-rule-cpps ref="deleteCouponRuleCpps" @success="handleSuccess" />
  </div>
</template>

<script>
import { listCouponRuleCpps } from '@/api/scm/couponRuleApi'
import AddCouponRuleCpps from './AddCouponRuleCpps'
import EditCouponRuleCpps from './EditCouponRuleCpps'
import DeleteCouponRuleCpps from './DeleteCouponRuleCpps'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CouponRuleCpps',
  components: {
    AddCouponRuleCpps,
    EditCouponRuleCpps,
    DeleteCouponRuleCpps
  },
  data() {
    return {
      ruleId:'',
      loading: false,
      tableData: [],
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
        const { data, total } = await listCouponRuleCpps(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to get coupon rule cpps:', error)
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
      this.$refs.addCouponRuleCpps.open()
    },
    openEditModal(row) {
      this.$refs.editCouponRuleCpps.open(row)
    },
    openDeleteModal(row) {
      this.$refs.deleteCouponRuleCpps.open(row)
    },
    handleSuccess() {
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.coupon-rule-cpps {
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