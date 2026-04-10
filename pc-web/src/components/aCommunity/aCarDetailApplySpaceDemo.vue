<template>
  <a-car-detail-apply-space ref="carDetailApply" />
</template>

<script>
import ACarDetailApplySpace from './aCarDetailApplySpace'

export default {
  components: {
    ACarDetailApplySpace
  },
  methods: {
    open(data) {
      this.$refs.carDetailApply.handleSwitch(data)
    }
  }
}
</script>