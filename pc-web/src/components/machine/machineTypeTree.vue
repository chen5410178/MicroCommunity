<template>
  <el-card class="tree-container">
    <el-tree ref="machineTypeTree" :data="treeData" node-key="id" :props="defaultProps" :highlight-current="true"
      :expand-on-click-node="false" @node-click="handleNodeClick"></el-tree>
  </el-card>
</template>

<script>
import { listMachineType } from '@/api/machine/machineTypeTreeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MachineTypeTree',
  props: {
    state: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadMachineTypesTree()
  },
  methods: {
    async loadMachineTypesTree() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          state: this.state
        }
        const { data } = await listMachineType(params)
        this.treeData = this.buildTree(data)
      } catch (error) {
        console.error('加载设备类型树失败:', error)
      }
    },
    buildTree(data) {
      const result = []
      const map = {}

      if (!Array.isArray(data)) {
        return result
      }

      data.forEach(item => {
        map[item.typeId] = {
          id: item.typeId,
          typeId: item.typeId,
          parentTypeId: item.parentTypeId,
          text: item.machineTypeName,
          children: []
        }
      })

      data.forEach(item => {
        const parent = map[item.parentTypeId]
        if (parent) {
          parent.children.push(map[item.typeId])
        } else {
          result.push(map[item.typeId])
        }
      })

      return result
    },
    handleNodeClick(data) {
      this.$emit('switchType', {
        typeId: data.typeId,
        typeName: data.text
      })
    },
    refreshTree() {
      this.loadMachineTypesTree()
    }
  }
}
</script>

<style lang="scss" scoped>
.tree-container {
  height: 100%;

  .el-tree {
    height: 100%;
    overflow: auto;
  }
}
</style>