<template>
  <car-detail-owner ref="carDetailOwner"></car-detail-owner>
</template>

<script>
import CarDetailOwner from './carDetailOwner'

export default {
  name: 'CarDetailOwnerDemo',
  components: {
    CarDetailOwner
  },
  mounted() {
    this.$refs.carDetailOwner.$emit('switch', {
      ownerId: '123'
    })
  }
}
</script>