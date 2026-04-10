<template>
  <div class="printer-rule-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <printer-rule-div @switchRule="handleSwitchRule" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="text-left">
            <span>{{ printerRuleInfo.curPrinterRule.ruleName }}</span>
            <div class="role-context">{{ printerRuleInfo.curPrinterRule.remark }}</div>
          </div>
          <div class="role-menu flex justify-start">
            <el-button :type="printerRuleInfo.tabName === 'printerRuleMachine' ? 'primary' : 'text'"
              @click="_changePrinterRuleTab('printerRuleMachine')">
              {{ $t('printerRule.tab.machine') }}
            </el-button>
            <el-button :type="printerRuleInfo.tabName === 'printerRuleFees' ? 'primary' : 'text'"
              @click="_changePrinterRuleTab('printerRuleFees')">
              {{ $t('printerRule.tab.fees') }}
            </el-button>
            <el-button :type="printerRuleInfo.tabName === 'printerRuleRepair' ? 'primary' : 'text'"
              @click="_changePrinterRuleTab('printerRuleRepair')">
              {{ $t('printerRule.tab.repair') }}
            </el-button>
          </div>

          <div v-if="printerRuleInfo.tabName === 'printerRuleMachine'">
            <printer-rule-machine ref="printerRuleMachine" />
          </div>
          <div v-if="printerRuleInfo.tabName === 'printerRuleFees'">
            <printer-rule-fees ref="printerRuleFees"/>
          </div>
          <div v-if="printerRuleInfo.tabName === 'printerRuleRepair'">
            <printer-rule-repair ref="printerRuleRepair"/>
          </div>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import PrinterRuleDiv from '@/components/machine/printerRuleDiv'
import PrinterRuleMachine from '@/components/machine/printerRuleMachine'
import PrinterRuleFees from '@/components/machine/printerRuleFees'
import PrinterRuleRepair from '@/components/machine/printerRuleRepair'


export default {
  name: 'PrinterRuleList',
  components: {
    PrinterRuleDiv,
    PrinterRuleMachine,
    PrinterRuleFees,
    PrinterRuleRepair,

  },
  data() {
    return {
      printerRuleInfo: {
        curPrinterRule: {},
        tabName: 'printerRuleMachine'
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    handleSwitchRule(rule) {
      this.printerRuleInfo.curPrinterRule = rule
      this._changePrinterRuleTab('printerRuleMachine')
    },
    _changePrinterRuleTab(tabName) {
      this.printerRuleInfo.tabName = tabName
      setTimeout(() => {
        this.$refs[tabName].open(this.printerRuleInfo.curPrinterRule.ruleId)
      },500)
      
    },
    handleSuccess() {
      this.$refs.printerRuleDiv.refreshList()
    }
  }
}
</script>

<style lang="scss" scoped>
.printer-rule-container {
  padding: 20px;

  .line-x {
    border-top: 1px solid #eee;
    margin: 15px 0;
  }

  .role-menu {
    margin-bottom: 20px;

    .el-button {
      margin-right: 15px;
    }
  }

  .role-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .role-context {
    color: #666;
    font-size: 14px;
  }
}
</style>