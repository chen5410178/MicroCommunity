<template>
  <div>

    <div class="bg-white margin-top-xs padding border-radius tree-div">
      <el-tree :data="treeData" :props="defaultProps" default-expand-all @node-click="handleNodeClick"></el-tree>
    </div>

  </div>
</template>

<script>
import { listResourceStoreTypeTree } from '@/api/resource/resourceStoreManageApi'

export default {
  name: 'ResourceStoreTypeTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  created() {
    this._loadResourceStoreTypeTree()
  },
  methods: {
    refreshTree() {
      this._loadResourceStoreTypeTree()
    },
    async _loadResourceStoreTypeTree() {
      try {
        const { data } = await listResourceStoreTypeTree()
        this.treeData = this._doTypeJsTreeData(data)
      } catch (error) {
        console.log('请求失败处理')
      }
    },
    _doTypeJsTreeData(types) {
      let mFloorTree = []
      if (types && types.length > 0) {
        types.forEach(pItem => {
          let floorItem = {
            id: 'f_' + pItem.parentRstId,
            name: pItem.parentName,
            children: []
          }
          if (pItem.subTypes) {
            pItem.subTypes.forEach(subType => {
              let menuItem = {
                id: 'u_' + subType.rstId,
                name: subType.name
              }
              floorItem.children.push(menuItem)
            })
          }
          mFloorTree.push(floorItem)
        })
      }
      return mFloorTree
    },
    handleNodeClick(data) {
      if (data.id.startsWith('f_')) {
        this.$emit('switchParent', { parentRstId: data.id.replace('f_', '') })
      } else {
        this.$emit('switchRst', { rstId: data.id.replace('u_', '') })
      }
    }
  }
}
</script>

<style scoped>
.tree-div {
  background-color: white;
  margin-top: 10px;
  padding: 10px;
  border-radius: 4px;
}
</style>