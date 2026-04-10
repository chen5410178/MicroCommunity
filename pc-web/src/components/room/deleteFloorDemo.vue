<template>
  <div>
    <el-button @click="openDeleteModal">Delete Building</el-button>
    <delete-floor ref="deleteModal" @refresh-data="refreshData" @refresh-tree="refreshTree" />
  </div>
</template>

<script>
import DeleteFloor from '../deleteFloor'

export default {
  components: {
    DeleteFloor
  },
  methods: {
    openDeleteModal() {
      // 模拟楼栋数据
      const buildingData = {
        floorId: '123',
        floorNum: 'A1'
      }
      this.$refs.deleteModal.open(buildingData)
    },
    refreshData() {
      console.log('Refresh building list')
    },
    refreshTree() {
      console.log('Refresh building tree')
    }
  }
}
</script>