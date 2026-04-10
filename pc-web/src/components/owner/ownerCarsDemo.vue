<template>
  <div>
    <el-button @click="openOwnerCars">查看业主车辆</el-button>
    <owner-cars ref="ownerCarsRef" />
  </div>
</template>

<script>
import OwnerCars from './ownerCars'

export default {
  components: {
    OwnerCars
  },
  methods: {
    openOwnerCars() {
      this.$refs.ownerCarsRef.open('456') // 传递业主ID
    }
  }
}
</script>