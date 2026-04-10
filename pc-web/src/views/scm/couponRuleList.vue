<template>
  <div class="coupon-rule-container">
    <el-row class="coupon-rule-wrapper">
      <el-col :span="4" class="left-panel">
        <coupon-rule-div ref="couponRuleDiv" @switch="handleSwitchCouponRule" />
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-card class="box-card">
          <div slot="header" class="text-left">
            <span>{{ currentCouponRule.ruleName }}</span>
            <div class="role-context">{{ currentCouponRule.remark }}</div>
          </div>
          <el-tabs v-model="activeTab" @tab-click="handleTabClick(activeTab)">
            <el-tab-pane :label="$t('couponRule.coupon')" name="couponRuleCpps">
              <coupon-rule-cpps ref="couponRuleCpps" v-if="activeTab === 'couponRuleCpps'"  />
            </el-tab-pane>
            <el-tab-pane :label="$t('couponRule.fee')" name="couponRuleFee">
              <coupon-rule-fees ref="couponRuleFee" v-if="activeTab === 'couponRuleFee'"  />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

  
  </div>
</template>

<script>
import CouponRuleDiv from '@/components/scm/CouponRuleDiv'
import CouponRuleCpps from '@/components/scm/CouponRuleCpps'
import CouponRuleFees from '@/components/scm/CouponRuleFees'


export default {
  name: 'CouponRuleList',
  components: {
    CouponRuleDiv,
    CouponRuleCpps,
    CouponRuleFees,

  },
  data() {
    return {
      activeTab: 'couponRuleCpps',
      currentCouponRule: {
        ruleId: '',
        ruleName: '',
        remark: ''
      }
    }
  },
  methods: {
    handleSwitchCouponRule(rule) {
      this.currentCouponRule = rule
      this.handleTabClick(this.activeTab)
    },

    handleTabClick(tab) {
      this.activeTab = tab
      let _that = this;
      setTimeout(function(){
        _that.$refs[tab].open(_that.currentCouponRule.ruleId)
      },500)
    },
   
 
  }
}
</script>

<style lang="scss" scoped>
.coupon-rule-container {
  padding: 20px;
  height: 100%;
  
  .coupon-rule-wrapper {
    height: 100%;
    
    .left-panel {
      padding-right: 10px;
      height: 100%;
    }
    
    .right-panel {
      height: 100%;
      
      .box-card {
        height: 100%;
        
        .clearfix {
          h5 {
            margin: 0;
            font-size: 16px;
            font-weight: bold;
          }
          
          .role-context {
            margin-top: 5px;
            color: #999;
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>