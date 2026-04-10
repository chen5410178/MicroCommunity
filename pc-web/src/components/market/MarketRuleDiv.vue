<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-button-group>
          <el-button type="primary" size="small" @click="openAddMarketRuleModal">{{ $t('marketRule.add') }}</el-button>
          <el-button type="primary" size="small" @click="openEditMarketRuleModal">{{ $t('marketRule.edit') }}</el-button>
          <el-button type="danger" size="small" @click="openDeleteMarketRuleModal">{{ $t('marketRule.delete')
          }}</el-button>
        </el-button-group>
      </div>
      <el-tree :data="marketRules" :props="defaultProps" node-key="ruleId" highlight-current
        @node-click="handleNodeClick"></el-tree>
    </el-card>

    <add-market-rule ref="addMarketRule" @success="handleSuccess" />
    <edit-market-rule ref="editMarketRule" @success="handleSuccess" />
    <delete-market-rule ref="deleteMarketRule" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMarketRule } from '@/api/market/marketRuleApi'
import AddMarketRule from '@/components/market/AddMarketRule'
import EditMarketRule from '@/components/market/EditMarketRule'
import DeleteMarketRule from '@/components/market/DeleteMarketRule'

export default {
  name: 'MarketRuleDiv',
  data() {
    return {
      marketRules: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      curMarketRule: {}
    }
  },
  components: {
    
    AddMarketRule,
    EditMarketRule,
    DeleteMarketRule
  },
  created() {
    this.listMarketRules()
  },
  methods: {
    async listMarketRules() {
      try {
        const { data } = await listMarketRule({
          page: 1,
          row: 100
        })
        this.marketRules = data
        if (this.marketRules.length > 0) {
          this.curMarketRule = this.marketRules[0]
          this.$emit('switchMarketRule', this.curMarketRule)
        }
      } catch (error) {
        this.$message.error(this.$t('marketRule.fetchError'))
      }
    },
    handleNodeClick(data) {
      this.curMarketRule = data
      this.$emit('switchMarketRule', data)
    },
    openAddMarketRuleModal() {
      this.$refs.addMarketRule && this.$refs.addMarketRule.open()
    },
    openEditMarketRuleModal() {
      if (!this.curMarketRule || !this.curMarketRule.ruleId) {
        this.$message.warning(this.$t('marketRule.selectRuleFirst'))
        return
      }
      this.$refs.editMarketRule && this.$refs.editMarketRule.open(this.curMarketRule)
    },
    openDeleteMarketRuleModal() {
      if (!this.curMarketRule || !this.curMarketRule.ruleId) {
        this.$message.warning(this.$t('marketRule.selectRuleFirst'))
        return
      }
      this.$refs.deleteMarketRule && this.$refs.deleteMarketRule.open(this.curMarketRule)
    },
    
    handleSuccess() {
      this.listMarketRules()
    }
  }
}
</script>

<style scoped>
.box-card {
  height: 100%;
}

.el-tree {
  margin-top: 10px;
}
</style>