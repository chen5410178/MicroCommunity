<template>
  <div class="community-inspection-tree">
    <el-card class="box-card">
      <el-tree
        ref="tree"
        :data="treeData"
        node-key="id"
        :props="defaultProps"
        :highlight-current="true"
        :expand-on-click-node="false"
        @node-click="handleNodeClick"
      ></el-tree>
    </el-card>
  </div>
</template>

<script>
import { queryCommunityInspectionTree } from '@/api/inspection/adminInspectionTaskApi'

export default {
  name: 'CommunityInspectionTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  mounted() {
    this.loadTreeData()
  },
  methods: {
    async loadTreeData() {
      try {
        const response = await queryCommunityInspectionTree({ hc: 1.8 })
        this.treeData = response.data || []
      } catch (error) {
        console.error('Failed to load tree data:', error)
      }
    },
    handleNodeClick(data, node) {
      console.log(node)
      const params = {
        communityId: '',
        inspectionPlanId: '',
        staffId: ''
      }

      if (data.id.startsWith('c_')) {
        params.communityId = data.original.communityId
        params.communityName = data.original.communityName
      } else if (data.id.startsWith('p_')) {
        params.communityId = data.original.communityId
        params.inspectionPlanId = data.original.inspectionPlanId
      } else if (data.id.startsWith('s_')) {
        params.communityId = data.original.communityId
        params.inspectionPlanId = data.original.inspectionPlanId
        params.staffId = data.original.staffId
      }

      this.$emit('notifyQuery', params)
    }
  }
}
</script>

<style lang="scss" scoped>
.community-inspection-tree {
  .box-card {
    border-radius: 4px;
  }
}
</style>