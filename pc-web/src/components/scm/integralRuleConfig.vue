<template>
  <div class="integral-rule-config">
    <el-row class="operation-bar">
      <el-col :span="24" class="text-right">
        <el-button-group>
          <el-button type="primary" size="small" @click="handleQuery">{{ $t('common.query') }}</el-button>
          <el-button type="primary" size="small" @click="openAddModal">{{ $t('common.add') }}</el-button>
        </el-button-group>
      </el-col>
    </el-row>

    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="configId" :label="$t('integralRule.configId')" width="100" align="center" />
      <el-table-column prop="configName" :label="$t('integralRule.configName')" align="center" />
      <el-table-column prop="computingFormulaName" :label="$t('integralRule.computingFormula')" align="center" />
      <el-table-column prop="squarePrice" :label="$t('integralRule.squarePrice')" align="center" />
      <el-table-column prop="additionalAmount" :label="$t('integralRule.additionalAmount')" align="center" />
      <el-table-column prop="scaleName" :label="$t('integralRule.scale')" align="center" />
      <el-table-column :label="$t('common.operation')" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />

    <add-integral-rule-config ref="addIntegralRuleConfig" :rule-id="ruleId" @success="handleSuccess" />
  </div>
</template>

<script>
import { listIntegralRuleConfig, deleteIntegralRuleConfig } from '@/api/scm/integralRuleApi'
import AddIntegralRuleConfig from './addIntegralRuleConfig'

export default {
  name: 'IntegralRuleConfig',
  components: {
    AddIntegralRuleConfig
  },
  data() {
    return {
      loading: false,
      ruleId: '',
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
        const { data, total } = await listIntegralRuleConfig(params)
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
      this.$refs.addIntegralRuleConfig.open()
    },
    async handleDelete(row) {
      try {
        await this.$confirm(
          this.$t('integralRule.deleteConfigConfirm'),
          this.$t('common.tip'),
          {
            confirmButtonText: this.$t('common.confirm'),
            cancelButtonText: this.$t('common.cancel'),
            type: 'warning'
          }
        )
        await deleteIntegralRuleConfig({
          configId: row.configId,
          ruleId: this.ruleId,
          ircId: row.ircId
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
.integral-rule-config {
  padding: 0px;
}

.operation-bar {
  margin-bottom: 20px;
}
</style>