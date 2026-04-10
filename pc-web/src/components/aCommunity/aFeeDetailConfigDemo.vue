<template>
  <a-fee-detail-config ref="feeConfigComponent" />
</template>

<script>
import AFeeDetailConfig from './aFeeDetailConfig'

export default {
  components: {
    AFeeDetailConfig
  },
  methods: {
    open(data) {
      this.$refs.feeConfigComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.feeConfigComponent.handleNotify()
    }
  }
}
</script>