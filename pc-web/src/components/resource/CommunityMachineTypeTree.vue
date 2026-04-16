<template>
  <div class="tree-container">
    <el-card class="box-card">
      <el-tree
        ref="machineTypeTree"
        :data="treeData"
        node-key="id"
        :props="defaultProps"
        :highlight-current="true"
        :expand-on-click-node="false"
        @node-click="handleNodeClick"
      />
    </el-card>
  </div>
</template>

<script>
import { queryCommunityMachineTypeTree } from '@/api/resource/adminEquipmentApi'

export default {
  name: 'CommunityMachineTypeTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      callName: ''
    }
  },
  mounted() {
    this.initTree()
  },
  methods: {
    async initTree() {
      await this.loadCommunityMachineTypes()
    },
    async loadCommunityMachineTypes() {
      try {
        const { data } = await queryCommunityMachineTypeTree()
        this.treeData = data
      } catch (error) {
        console.error('Failed to load machine types:', error)
      }
    },
    handleNodeClick(data, node) {
      console.log(node)
      if (data.id.startsWith('c_')) {
        this.$emit('selectMachineType', {
          communityId: data.communityId,
          typeId: ''
        })
      } else if (data.id.startsWith('m_')) {
        this.$emit('selectMachineType', {
          communityId: data.communityId,
          typeId: data.typeId
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.tree-container {
  height: 100%;

  .box-card {
    height: 100%;
  }
}
</style>