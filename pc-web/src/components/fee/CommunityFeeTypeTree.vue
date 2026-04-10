<template>
  <el-card class="tree-card">
    <el-tree
      ref="feeTypeTree"
      :data="treeData"
      node-key="id"
      :props="defaultProps"
      :highlight-current="true"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script>
import { queryCommunityFeeTypeTree } from '@/api/fee/adminFeeConfigApi'

export default {
  name: 'CommunityFeeTypeTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  methods: {
    async loadTreeData() {
      try {
        const { data } = await queryCommunityFeeTypeTree({ hc: 1.8 })
        this.treeData = data
       
      } catch (error) {
        this.$message.error(this.$t('adminFeeConfig.tree.fetchError'))
      }
    },
    handleNodeClick(data) {
      if (data.id.startsWith('c_')) {
        this.$emit('selectCommunity', {
          communityName: data.communityName,
          communityId: data.communityId
        })
      } else if (data.id.startsWith('f_')) {
        this.$emit('selectFeeTypeCd', {
          feeTypeCd: data.feeTypeCd,
          communityId: data.communityId
        })
      } else if (data.id.startsWith('l_')) {
        this.$emit('selectFeeFlag', {
          feeFlag: data.feeFlag,
          feeTypeCd: data.feeTypeCd,
          communityId: data.communityId
        })
      }
    },
    open() {
      this.loadTreeData()
    }
  }
}
</script>

<style scoped>
.tree-card {
  max-height: 80vh;
  overflow-y: auto;
}
</style>