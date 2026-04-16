<template>
  <el-card class="org-tree-card">
    <el-tree
      ref="orgTree"
      :data="orgTreeData"
      :props="defaultProps"
      node-key="id"
      highlight-current
      default-expand-all
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script>
import { queryAdminOrgTree } from '@/api/staff/storeStaffApi'

export default {
  name: 'StoreOrgTree',
  data() {
    return {
      orgTreeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  created() {
    this.fetchOrgTree()
  },
  methods: {
    async fetchOrgTree() {
      let _dataTree = [{
        id:'1',
        text:'全部',
        children: []
      }]
      try {
        const { data } = await queryAdminOrgTree({ hc: 1.8 })
        _dataTree[0].children = data
        this.orgTreeData = _dataTree
      } catch (error) {
        this.$message.error(this.$t('storeOrgTree.fetchError'))
      }
    },
    handleNodeClick(data) {
      this.$emit('switchOrg', {
        orgId: data.id,
        orgName: data.text
      })
    },
    refreshTree() {
      this.fetchOrgTree()
    }
  }
}
</script>

<style lang="scss" scoped>
.org-tree-card {
  height: 100%;
  
  .el-tree {
    height: 100%;
    overflow: auto;
  }
}
</style>