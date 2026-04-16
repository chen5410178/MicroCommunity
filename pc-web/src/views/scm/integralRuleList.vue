<template>
  <div class="integral-rule-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <integral-rule-div ref="integralRuleDiv" @switch="handleSwitchRule" />
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-card class="box-card">
          <div slot="header" class="text-left">
            <span>{{ integralRuleInfo.curIntegralRule.ruleName }}</span>
            <div class="role-context">{{ integralRuleInfo.curIntegralRule.remark }}</div>
          </div>
          <el-tabs v-model="integralRuleInfo.tabName" @tab-click="handleTabClick(integralRuleInfo.tabName)">
            <el-tab-pane label="积分标准" name="integralRuleConfig">
              <integral-rule-config  ref="integralRuleConfig" v-if="integralRuleInfo.tabName === 'integralRuleConfig'"
                />
            </el-tab-pane>
            <el-tab-pane label="缴费时间" name="integralRuleFee">
              <integral-rule-fees ref="integralRuleFee" v-if="integralRuleInfo.tabName === 'integralRuleFee'"
                />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import IntegralRuleDiv from '@/components/scm/integralRuleDiv'
import IntegralRuleConfig from '@/components/scm/integralRuleConfig'
import IntegralRuleFees from '@/components/scm/integralRuleFees'


export default {
  name: 'IntegralRuleList',
  components: {
    IntegralRuleDiv,
    IntegralRuleConfig,
    IntegralRuleFees,

  },
  data() {
    return {
      integralRuleInfo: {
        curIntegralRule: {},
        tabName: 'integralRuleConfig'
      }
    }
  },
  methods: {
    handleSwitchRule(rule) {
      this.integralRuleInfo.curIntegralRule = rule
      this.handleTabClick(this.integralRuleInfo.tabName)
    },
    handleTabClick(tab) {
      this.integralRuleInfo.tabName = tab
      setTimeout(() => {
        this.$refs[tab].open(this.integralRuleInfo.curIntegralRule)
      }, 500);
    },

  
  }
}
</script>

<style lang="scss" scoped>
.integral-rule-container {
  padding: 20px;
  height: 100%;

  .left-panel {
    padding-right: 0;
  }

  .right-panel {
    padding-left: 20px;
  }

  .box-card {
    height: 100%;

    .clearfix {

      &:before,
      &:after {
        display: table;
        content: "";
      }

      &:after {
        clear: both;
      }
    }

    .role-title {
      margin: 0;
      font-size: 16px;
      font-weight: bold;
    }

    .role-context {
      margin-top: 10px;
      color: #666;
    }
  }
}
</style>