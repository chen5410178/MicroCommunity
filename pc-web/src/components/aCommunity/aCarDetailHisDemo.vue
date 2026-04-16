<template>
  <a-car-detail-his ref="carDetailHis" />
</template>

<script>
import ACarDetailHis from './aCarDetailHis'

export default {
  components: {
    ACarDetailHis
  },
  methods: {
    open(data) {
      this.$refs.carDetailHis.handleSwitch(data)
    }
  }
}
</script>