<template>
  <div class="integral-rule-fees">
    <el-row class="operation-bar">
      <el-col :span="24" class="text-right">
        <el-button-group>
          <el-button type="primary" size="small" @click="handleQuery">{{ $t('common.query') }}</el-button>
          <el-button type="primary" size="small" @click="openAddModal">{{ $t('common.add') }}</el-button>
        </el-button-group>
      </el-col>
    </el-row>

    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="irfId" :label="$t('integralRule.feeId')" width="100" align="center" />
      <el-table-column :label="$t('integralRule.payTimeRange')" align="center">
        <template slot-scope="scope">
          {{ scope.row.payStartTime }} ~ {{ scope.row.payEndTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" width="200" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />

    <add-integral-rule-fee ref="addIntegralRuleFee" :rule-id="ruleId" @success="handleSuccess" />
    <edit-integral-rule-fee ref="editIntegralRuleFee" @success="handleSuccess" />
  </div>
</template>

<script>
import { listIntegralRuleFee, deleteIntegralRuleFee } from '@/api/scm/integralRuleApi'
import AddIntegralRuleFee from './addIntegralRuleFee'
import EditIntegralRuleFee from './editIntegralRuleFee'

export default {
  name: 'IntegralRuleFees',
  components: {
    AddIntegralRuleFee,
    EditIntegralRuleFee
  },
  data() {
    return {
      ruleId: '',
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
    open(rule) {
      this.ruleId = rule.ruleId
      this.handleQuery()
    },
    async fetchData() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ruleId: this.ruleId
        }
        const { data, total } = await listIntegralRuleFee(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.pagination.current = 1
      this.fetchData()
    },
    openAddModal() {
      console.log(this.ruleId)
      this.$refs.addIntegralRuleFee.open({
        ruleId: this.ruleId
      })
    },
    handleEdit(row) {
      this.$refs.editIntegralRuleFee.open(row)
    },
    async handleDelete(row) {
      try {
        await this.$confirm(
          this.$t('integralRule.deleteFeeConfirm'),
          this.$t('common.tip'),
          {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }
        )

        await deleteIntegralRuleFee({
          irfId: row.irfId,
          ruleId: this.ruleId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleQuery()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(error.message || this.$t('common.deleteFailed'))
        }
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },
    handleSuccess() {
      this.handleQuery()
    }
  }
}
</script>

<style scoped>
.integral-rule-fees {
  padding: 0px;
}

.operation-bar {
  margin-bottom: 20px;
}
</style>