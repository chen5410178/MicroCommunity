<template>
  <div>
    <el-card class="coupon-rule-div">
      <div class="operation-buttons">
        <el-button type="primary" size="small" @click="openAddModal">{{ $t('common.add') }}</el-button>
        <el-button type="warning" size="small" @click="openEditModal">{{ $t('common.edit') }}</el-button>
        <el-button type="danger" size="small" @click="openDeleteModal">{{ $t('common.delete') }}</el-button>
      </div>

      <el-divider></el-divider>

      <el-tree :data="couponRules" :props="treeProps" node-key="ruleId" highlight-current @node-click="handleNodeClick"
        class="coupon-rule-tree">
        <span slot-scope="{ node }" class="custom-tree-node">
          <span>{{ node.label }}</span>
        </span>
      </el-tree>
    </el-card>
    <add-coupon-rule ref="addCouponRule" @success="handleSuccess" />
    <edit-coupon-rule ref="editCouponRule" @success="handleSuccess" />
    <delete-coupon-rule ref="deleteCouponRule" @success="handleSuccess" />
  </div>
</template>

<script>
import { listCouponRule } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddCouponRule from '@/components/scm/AddCouponRule'
import EditCouponRule from '@/components/scm/EditCouponRule'
import DeleteCouponRule from '@/components/scm/DeleteCouponRule'
export default {
  name: 'CouponRuleDiv',
  data() {
    return {
      couponRules: [],
      currentRule: {},
      treeProps: {
        label: 'ruleName',
        children: 'children'
      }
    }
  },
  components: {
    AddCouponRule,
    EditCouponRule,
    DeleteCouponRule
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listCouponRule(params)
        this.couponRules = data
        if (this.couponRules.length > 0) {
          this.handleNodeClick(this.couponRules[0])
        }
      } catch (error) {
        console.error('Failed to get coupon rules:', error)
      }
    },
    handleNodeClick(data) {
      this.currentRule = data
      this.$emit('switch',this.currentRule);
    },
    openAddModal() {
      this.$refs.addCouponRule.open()
    },
    openEditModal() {
      if (!this.currentRule.ruleId) {
        this.$message.warning(this.$t('couponRule.selectRuleFirst'))
        return
      }
      this.$refs.editCouponRule.open(this.currentRule)
    },
    openDeleteModal() {
      if (!this.currentRule.ruleId) {
        this.$message.warning(this.$t('couponRule.selectRuleFirst'))
        return
      }
      this.$refs.deleteCouponRule.open(this.currentRule)
    },
    handleSuccess() {
      this.getList()
    },
    refreshList() {
    
    }
  }
}
</script>

<style lang="scss" scoped>
.coupon-rule-div {
  height: 100%;

  .operation-buttons {
    margin-bottom: 10px;

    .el-button {
      margin-right: 10px;
    }
  }

  .coupon-rule-tree {
    height: calc(100% - 60px);
    overflow-y: auto;

    .custom-tree-node {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 14px;
      padding-right: 8px;
    }
  }
}
</style>