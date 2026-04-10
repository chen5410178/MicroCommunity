<template>
  <a-fee-detail-his ref="feeHisComponent" />
</template>

<script>
import AFeeDetailHis from './aFeeDetailHis'

export default {
  components: {
    AFeeDetailHis
  },
  methods: {
    open(data) {
      this.$refs.feeHisComponent.handleSwitch(data)
    },
    refresh() {
      this.$refs.feeHisComponent.handleNotify()
    }
  }
}
</script>