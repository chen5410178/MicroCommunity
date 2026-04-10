<template>
  <floor-unit-tree
    :call-back-listener="callbackListener"
    @switchFloor="handleSwitchFloor"
    @switchUnit="handleSwitchUnit"
  />
</template>

<script>
import FloorUnitTree from '@/components/room/floorUnitTree'

export default {
  components: {
    FloorUnitTree
  },
  data() {
    return {
      callbackListener: 'demoFloorUnitTree'
    }
  },
  methods: {
    handleSwitchFloor(data) {
      console.log('切换楼栋', data.floorId)
    },
    handleSwitchUnit(data) {
      console.log('切换单元', data.unitId)
    }
  }
}
</script>