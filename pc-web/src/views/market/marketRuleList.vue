<template>
  <div class="market-rule-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <market-rule-div ref="marketRuleDiv" @switchMarketRule="handleSwitchMarketRule" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="clearfix text-left">
            <span>{{ marketRuleInfo.curMarketRule.name }}</span>
            <div class="role-context">{{ marketRuleInfo.curMarketRule.description }}</div>
          </div>
          <el-tabs v-model="marketRuleInfo.tabName" @tab-click="handleTabClick">
            <el-tab-pane label="营销方式" name="marketRuleWay">
              <market-rule-way v-if="marketRuleInfo.tabName === 'marketRuleWay'"
                :rule-id="marketRuleInfo.curMarketRule.ruleId" />
            </el-tab-pane>
            <el-tab-pane label="小区授权" name="marketRuleCommunity">
              <market-rule-community v-if="marketRuleInfo.tabName === 'marketRuleCommunity'"
                :rule-id="marketRuleInfo.curMarketRule.ruleId" />
            </el-tab-pane>
            <el-tab-pane label="作用对象" name="marketRuleObj">
              <market-rule-obj v-if="marketRuleInfo.tabName === 'marketRuleObj'"
                :rule-id="marketRuleInfo.curMarketRule.ruleId" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import MarketRuleDiv from '@/components/market/MarketRuleDiv'
import MarketRuleWay from '@/components/market/MarketRuleWay'
import MarketRuleCommunity from '@/components/market/MarketRuleCommunity'
import MarketRuleObj from '@/components/market/MarketRuleObj'


export default {
  name: 'MarketRuleList',
  components: {
    MarketRuleDiv,
    MarketRuleWay,
    MarketRuleCommunity,
    MarketRuleObj,
  },
  data() {
    return {
      marketRuleInfo: {
        curMarketRule: {},
        tabName: 'marketRuleWay'
      }
    }
  },
  methods: {
    handleSwitchMarketRule(marketRule) {
      this.marketRuleInfo.curMarketRule = marketRule
      this.marketRuleInfo.tabName = 'marketRuleWay'
    },
    handleTabClick(tab) {
      this.marketRuleInfo.tabName = tab.name
    },
    handleSuccess() {
      this.$refs.marketRuleDiv.listMarketRules()
    }
  }
}
</script>

<style lang="scss" scoped>
.market-rule-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;

    .clearfix {
      h5 {
        margin: 0;
        font-size: 18px;
      }

      .role-context {
        margin-top: 10px;
        color: #666;
      }
    }
  }
}</style>