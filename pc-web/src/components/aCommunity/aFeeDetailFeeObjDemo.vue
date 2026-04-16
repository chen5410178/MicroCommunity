<template>
  <a-fee-detail-fee-obj ref="feeObjComponent" />
</template>

<script>
import AFeeDetailFeeObj from './aFeeDetailFeeObj'

export default {
  components: {
    AFeeDetailFeeObj
  },
  methods: {
    open(data) {
      this.$refs.feeObjComponent.handleSwitch(data)
    }
  }
}
</script>