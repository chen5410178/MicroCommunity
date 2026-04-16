<template>
  <div class="market-rule-obj">
    <div class="action-bar">
      <el-button type="primary" size="small" @click="openAddMarketRuleObjModal">
        {{ $t('marketRule.associateObject') }}
      </el-button>
    </div>

    <el-table :data="marketRuleObjs" border style="width: 100%" v-loading="loading">
      <el-table-column prop="objTypeName" :label="$t('marketRule.objectName')" align="center" />
      <el-table-column prop="remark" :label="$t('marketRule.remark')" align="center" />
      <el-table-column :label="$t('marketRule.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteMarketRuleObjModal(scope.row)">
            {{ $t('marketRule.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total" />

    <add-market-rule-obj ref="addMarketRuleObj" :rule-id="ruleId" @success="handleSuccess" />
    <delete-market-rule-obj ref="deleteMarketRuleObj" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketRuleObj } from '@/api/market/marketRuleApi'
import addMarketRuleObj from './AddMarketRuleObj'
import deleteMarketRuleObj from './DeleteMarketRuleObj'

export default {
  name: 'MarketRuleObj',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  components: {
    addMarketRuleObj,
    deleteMarketRuleObj
  },
  data() {
    return {
      loading: false,
      marketRuleObjs: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  watch: {
    ruleId: {
      immediate: true,
      handler() {
        this.listMarketRuleObjs()
      }
    }
  },
  methods: {
    async listMarketRuleObjs() {
      if (!this.ruleId) return

      this.loading = true
      try {
        const { data, total } = await listMarketRuleObj({
          page: this.page.current,
          row: this.page.size,
          ruleId: this.ruleId
        })
        this.marketRuleObjs = data
        this.page.total = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    },
    openAddMarketRuleObjModal() {
      this.$refs.addMarketRuleObj.open()
    },
    openDeleteMarketRuleObjModal(row) {
      this.$refs.deleteMarketRuleObj.open(row)
    },
    handleSuccess() {
      this.listMarketRuleObjs()
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listMarketRuleObjs()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listMarketRuleObjs()
    }
  }
}
</script>

<style scoped>
.action-bar {
  margin-bottom: 15px;
  text-align: left;
}
</style>