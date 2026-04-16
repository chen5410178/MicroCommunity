<template>
  <div class="org-tree-show">
    <el-tree
      ref="orgTree"
      :data="orgData"
      :props="defaultProps"
      node-key="id"
      default-expand-all
      highlight-current
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
import { listOrgTree } from '@/api/system/workflowSettingManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OrgTreeShow',
  data() {
    return {
      communityId: '',
      orgData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  methods: {
    async initTree() {
      this.communityId = getCommunityId()
      try {
        const { data } = await listOrgTree({
          communityId: this.communityId
        })
        this.orgData = data || []
      } catch (error) {
        this.$message.error(this.$t('orgTree.fetchError'))
      }
    },
    
    handleNodeClick(data) {
      this.$emit('switch-org', {
        orgId: data.id,
        orgName: data.text
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.org-tree-show {
  /deep/ .el-tree {
    .el-tree-node {
      .el-tree-node__content {
        height: 40px;
        
        &:hover {
          background-color: #f5f7fa;
        }
      }
      
      &.is-current {
        > .el-tree-node__content {
          background-color: #ecf5ff;
          color: #409eff;
        }
      }
    }
  }
}
</style>