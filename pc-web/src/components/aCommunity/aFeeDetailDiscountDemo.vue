<template>
  <a-fee-detail-discount ref="feeDiscountComponent" />
</template>

<script>
import AFeeDetailDiscount from './aFeeDetailDiscount'

export default {
  components: {
    AFeeDetailDiscount
  },
  methods: {
    open(data) {
      this.$refs.feeDiscountComponent.handleSwitch(data)
    }
  }
}
</script>