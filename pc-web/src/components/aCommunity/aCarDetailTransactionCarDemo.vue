<template>
  <a-car-detail-transaction-car ref="carDetailTransaction" />
</template>

<script>
import ACarDetailTransactionCar from './aCarDetailTransactionCar'

export default {
  components: {
    ACarDetailTransactionCar
  },
  methods: {
    open(data) {
      this.$refs.carDetailTransaction.handleSwitch(data)
    }
  }
}
</script>