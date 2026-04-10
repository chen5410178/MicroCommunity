<template>
  <el-card class="tree-card">
    <el-tree
      ref="repairTree"
      :data="treeData"
      node-key="id"
      :props="defaultProps"
      :default-expand-all="true"
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script>
import { queryCommunityRepairTree } from '@/api/work/adminRepairApi'

export default {
  name: 'CommunityRepairTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  created() {
    this.loadTreeData()
  },
  methods: {
    async loadTreeData() {
      try {
        const { data } = await queryCommunityRepairTree()
        this.treeData = data
      } catch (error) {
        this.$message.error(this.$t('adminRepair.treeFetchError'))
      }
    },
    handleNodeClick(data, node) {
      console.log(node)
      if (data.id.startsWith('c_')) {
        this.$emit('selectCommunity', {
          communityName: data.communityName,
          communityId: data.communityId
        })
      } else if (data.id.startsWith('r_')) {
        this.$emit('selectRepairSetting', {
          communityId: data.communityId,
          repairType: data.repairType
        })
      } else if (data.id.startsWith('s_')) {
        this.$emit('selectState', {
          communityId: data.communityId,
          repairType: data.repairType,
          state: data.state
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.tree-card {
  max-height: 80vh;
  overflow-y: auto;
}
</style>