<template>
  <a-car-detail-owner ref="carDetailOwner" />
</template>

<script>
import ACarDetailOwner from './aCarDetailOwner'

export default {
  components: {
    ACarDetailOwner
  },
  methods: {
    open(data) {
      this.$refs.carDetailOwner.handleSwitch(data)
    }
  }
}
</script>