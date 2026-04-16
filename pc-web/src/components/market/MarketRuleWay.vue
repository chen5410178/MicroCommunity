<template>
  <div class="market-rule-way">
    <div class="action-bar">
      <el-button type="primary" size="small" @click="openAddMarketRuleWayModal" v-if="marketRuleWays.length < 1">
        {{ $t('marketRule.associateMarketingMethod') }}
      </el-button>
    </div>

    <el-table :data="marketRuleWays" border style="width: 100%" v-loading="loading">
      <el-table-column prop="wayTypeName" :label="$t('marketRule.marketingType')" align="center" />
      <el-table-column prop="textName" :label="$t('marketRule.marketingMethod')" align="center">
        <template slot-scope="scope">
          {{ scope.row.textName || scope.row.picName || scope.row.goodsName }}
        </template>
      </el-table-column>
      <el-table-column prop="remark" :label="$t('marketRule.remark')" align="center" />
      <el-table-column :label="$t('marketRule.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteMarketRuleWayModal(scope.row)">
            {{ $t('marketRule.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total" />

    <add-market-rule-way ref="addMarketRuleWay" :rule-id="ruleId" @success="handleSuccess" />
    <delete-market-rule-way ref="deleteMarketRuleWay" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketRuleWay } from '@/api/market/marketRuleApi'
import addMarketRuleWay from './AddMarketRuleWay'
import deleteMarketRuleWay from './DeleteMarketRuleWay'
export default {
  name: 'MarketRuleWay',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  components: {
    addMarketRuleWay,
    deleteMarketRuleWay
  },
  data() {
    return {
      loading: false,
      marketRuleWays: [],
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
        this.listMarketRuleWays()
      }
    }
  },
  methods: {
    async listMarketRuleWays() {
      if (!this.ruleId) return

      this.loading = true
      try {
        const { data, total } = await listMarketRuleWay({
          page: this.page.current,
          row: this.page.size,
          ruleId: this.ruleId
        })
        this.marketRuleWays = data
        this.page.total = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    },
    openAddMarketRuleWayModal() {
      this.$refs.addMarketRuleWay.open()
    },
    openDeleteMarketRuleWayModal(row) {
      this.$refs.deleteMarketRuleWay.open(row)
    },
    handleSuccess() {
      this.listMarketRuleWays()
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listMarketRuleWays()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listMarketRuleWays()
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