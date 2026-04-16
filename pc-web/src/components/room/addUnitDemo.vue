<template>
  <div>
    <el-button type="primary" @click="openAddUnit">添加单元</el-button>
    <add-unit ref="addUnit" @refresh-tree="refreshTree" @refresh-data="refreshData" />
  </div>
</template>

<script>
import AddUnit from './addUnit'

export default {
  components: {
    AddUnit
  },
  methods: {
    openAddUnit() {
      this.$refs.addUnit.open({ floorId: '123' })
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