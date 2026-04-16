<template>
  <div>
    <el-button type="danger" @click="openDeleteUnit">删除单元</el-button>
    <delete-unit ref="deleteUnit" @refresh-tree="refreshTree" @refresh-data="refreshData" />
  </div>
</template>

<script>
import DeleteUnit from './deleteUnit'

export default {
  components: {
    DeleteUnit
  },
  methods: {
    openDeleteUnit() {
      const unitData = {
        floorId: '123',
        unitId: '456'
      }
      this.$refs.deleteUnit.open(unitData)
    },
    refreshTree(data) {
      console.log('刷新树结构', data)
    },
    refreshData(data) {
      console.log('刷新数据', data)
    }
  }
}
</script>