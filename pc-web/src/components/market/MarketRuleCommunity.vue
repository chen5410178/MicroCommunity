<template>
  <div class="market-rule-community">
    <div class="action-bar">
      <el-button type="primary" size="small" @click="openAddMarketRuleCommunityModal">
        {{ $t('marketRule.associateCommunity') }}
      </el-button>
    </div>

    <el-table :data="marketRuleCommunitys" border style="width: 100%" v-loading="loading">
      <el-table-column prop="communityName" :label="$t('marketRule.communityName')" align="center" />
      <el-table-column prop="communityId" :label="$t('marketRule.communityId')" align="center" />
      <el-table-column prop="remark" :label="$t('marketRule.remark')" align="center" />
      <el-table-column :label="$t('marketRule.operation')" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="openDeleteMarketRuleCommunityModal(scope.row)">
            {{ $t('marketRule.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total" />

    <add-market-rule-community ref="addMarketRuleCommunity" :rule-id="ruleId" @success="handleSuccess" />
    <delete-market-rule-community ref="deleteMarketRuleCommunity" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketRuleCommunity } from '@/api/market/marketRuleApi'
import AddMarketRuleCommunity from './AddMarketRuleCommunity'
import DeleteMarketRuleCommunity from './DeleteMarketRuleCommunity'

export default {
  name: 'MarketRuleCommunity',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  components: {
    AddMarketRuleCommunity,
    DeleteMarketRuleCommunity
  },
  data() {
    return {
      loading: false,
      marketRuleCommunitys: [],
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
        this.listMarketRuleCommunitys()
      }
    }
  },
  methods: {
    async listMarketRuleCommunitys() {
      if (!this.ruleId) return

      this.loading = true
      try {
        const { data, total } = await listMarketRuleCommunity({
          page: this.page.current,
          row: this.page.size,
          ruleId: this.ruleId
        })
        this.marketRuleCommunitys = data
        this.page.total = total
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    },
    openAddMarketRuleCommunityModal() {
      this.$refs.addMarketRuleCommunity.open()
    },
    openDeleteMarketRuleCommunityModal(row) {
      this.$refs.deleteMarketRuleCommunity.open(row)
    },
    handleSuccess() {
      this.listMarketRuleCommunitys()
    },
    handleSizeChange(size) {
      this.page.size = size
      this.listMarketRuleCommunitys()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this.listMarketRuleCommunitys()
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