<template>
  <div>

  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <el-button-group>
        <el-button size="small" @click="openAddModal">{{ $t('common.add') }}</el-button>
        <el-button size="small" @click="openEditModal">{{ $t('common.edit') }}</el-button>
        <el-button size="small" @click="openDeleteModal">{{ $t('common.delete') }}</el-button>
      </el-button-group>
    </div>
    <el-tree
      :data="integralRules"
      :props="defaultProps"
      node-key="ruleId"
      highlight-current
      @node-click="handleNodeClick"
    ></el-tree>
  </el-card>
  <add-integral-rule ref="addIntegralRule" @success="handleSuccess" />
    <edit-integral-rule ref="editIntegralRule" @success="handleSuccess" />
    <delete-integral-rule ref="deleteIntegralRule" @success="handleSuccess" />
  </div>
</template>

<script>
import { listIntegralRule } from '@/api/scm/integralRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddIntegralRule from '@/components/scm/addIntegralRule'
import EditIntegralRule from '@/components/scm/editIntegralRule'
import DeleteIntegralRule from '@/components/scm/deleteIntegralRule'
export default {
  name: 'IntegralRuleDiv',
  data() {
    return {
      integralRules: [],
      curIntegralRule: {},
      defaultProps: {
        children: 'children',
        label: 'ruleName'
      }
    }
  },
  components: {
    AddIntegralRule,
    EditIntegralRule,
    DeleteIntegralRule
  },
  created() {
    this.communityId = getCommunityId()
    this.refreshList()
  },
  methods: {
    async refreshList() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listIntegralRule(params)
        this.integralRules = data
        if (this.integralRules.length > 0) {
          this.handleNodeClick(this.integralRules[0])
        }
      } catch (error) {
        this.$message.error(this.$t('integralRule.fetchError'))
      }
    },
    handleNodeClick(data) {
      this.curIntegralRule = data
      this.$emit('switch', data)
    },
    openAddModal() {
      this.$refs.addIntegralRule.open()
    },
    openEditModal() {
      if (!this.curIntegralRule.ruleId) {
        this.$message.warning(this.$t('integralRule.selectRule'))
        return
      }
      this.$refs.editIntegralRule.open(this.curIntegralRule)
    },
    openDeleteModal() {
      if (!this.curIntegralRule.ruleId) {
        this.$message.warning(this.$t('integralRule.selectRule'))
        return
      }
      this.$refs.deleteIntegralRule.open(this.curIntegralRule)
    },
    handleSuccess() {
      this.refreshList()
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