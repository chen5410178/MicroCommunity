<template>
  <a-fee-detail-sub ref="feeSubComponent" />
</template>

<script>
import AFeeDetailSub from './aFeeDetailSub'

export default {
  components: {
    AFeeDetailSub
  },
  methods: {
    open(data) {
      this.$refs.feeSubComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.feeSubComponent.handleLoadSub()
    }
  }
}
</script>