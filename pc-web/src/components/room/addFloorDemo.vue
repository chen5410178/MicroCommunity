<template>
  <div>
    <el-button @click="openAddFloorModal">Add Building</el-button>
    <add-floor ref="addFloorModal" @refresh-data="refreshData" @refresh-tree="refreshTree" />
  </div>
</template>

<script>
import AddFloor from '../addFloor'

export default {
  components: {
    AddFloor
  },
  methods: {
    openAddFloorModal() {
      this.$refs.addFloorModal.open()
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