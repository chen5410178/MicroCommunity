<template>
  <div>
    <el-button @click="openEditModal">Edit Building</el-button>
    <edit-floor ref="editModal" @refresh-data="refreshData" @refresh-tree="refreshTree" />
  </div>
</template>

<script>
import EditFloor from '../editFloor'

export default {
  components: {
    EditFloor
  },
  methods: {
    openEditModal() {
      // 模拟楼栋数据
      const buildingData = {
        floorId: '123',
        floorNum: 'A1',
        floorName: 'A1 Building',
        floorArea: '3000',
        seq: 1,
        remark: 'Main building'
      }
      this.$refs.editModal.open(buildingData)
    },
    refreshData() {
      console.log('Refresh building list')
    },
    refreshTree(data) {
      console.log('Refresh building tree with floorId:', data.floorId)
    }
  }
}
</script>