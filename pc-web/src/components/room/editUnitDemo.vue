<template>
  <div>
    <el-button type="primary" @click="openEditUnit">编辑单元</el-button>
    <edit-unit ref="editUnit" @refresh-tree="refreshTree" @refresh-data="refreshData" />
  </div>
</template>

<script>
import EditUnit from './editUnit'

export default {
  components: {
    EditUnit
  },
  methods: {
    openEditUnit() {
      const unitData = {
        floorId: '123',
        unitId: '456',
        unitNum: 'A1',
        layerCount: 20,
        lift: '1010',
        unitArea: '3000',
        remark: '备注信息'
      }
      this.$refs.editUnit.open(unitData)
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