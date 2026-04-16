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
import { getCommunityId } from '@/api/community/communityApi'
import { listOrgTree } from '@/api/work/itemReleaseDetailApi'

export default {
  name: 'OrgTreeShow',
  data() {
    return {
      communityId: '',
      orgTreeShowInfo: {
        orgs: [],
        orgId: '',
        curOrg: {}
      },
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    refreshTree() {
      this._loadOrgsShow()
    },
    async _loadOrgsShow() {
      try {
        const { data } = await listOrgTree({
          communityId: this.communityId
        })
        this.orgTreeShowInfo.orgs = data
      } catch (error) {
        console.error('获取组织树失败:', error)
      }
    },
    handleNodeClick(data) {
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
  height: 400px;
  overflow-y: auto;
  
  /deep/ .el-tree {
    .el-tree-node__content {
      height: 40px;
    }
    
    .el-tree-node.is-current > .el-tree-node__content {
      background-color: #f0f7ff;
    }
  }
}
</style>