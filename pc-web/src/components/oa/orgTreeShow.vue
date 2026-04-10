<template>
  <div class="org-tree-container">
    <el-tree
      ref="orgTree"
      :data="orgTreeShowInfo.orgs"
      :props="defaultProps"
      node-key="id"
      default-expand-all
      highlight-current
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
import { listOrgTree } from '@/api/oa/newOaWorkflowDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OrgTreeShow',
  data() {
    return {
      orgTreeShowInfo: {
        orgs: [],
        orgId: '',
        curOrg: {}
      },
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    refresh() {
      this._loadOrgsShow()
    },
    async _loadOrgsShow() {
      try {
        const params = {
          communityId: this.communityId
        }
        const response = await listOrgTree(params)
        this.orgTreeShowInfo.orgs = response.data
      } catch (error) {
        console.error('加载组织树失败:', error)
      }
    },
    handleNodeClick(data, node) {
      this.orgTreeShowInfo.curOrg = data
      this.orgTreeShowInfo.curOrg.orgId = data.id
      this.$emit('switchOrg', {
        orgId: data.id,
        orgName: data.text
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.org-tree-container {
  height: 100%;
  
  /deep/ .el-tree {
    height: 100%;
    overflow-y: auto;
    
    .el-tree-node__content {
      height: 36px;
    }
  }
}
</style>